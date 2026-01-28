package com.fitlux.estore.service.auth;

import com.fitlux.estore.common.exception.BusinessException;
import com.fitlux.estore.common.exception.UnauthorizedException;
import com.fitlux.estore.config.Jwtutil;
import com.fitlux.estore.constants.serviceCodes.enums.RoleCode;
import com.fitlux.estore.constants.serviceCodes.enums.UserStatus;
import com.fitlux.estore.constants.serviceCodes.enums.serviceCodeImpl;
import com.fitlux.estore.dto.auth.request.LoginRequest;
import com.fitlux.estore.dto.auth.request.RefreshTokenRequest;
import com.fitlux.estore.dto.auth.request.SignupRequest;
import com.fitlux.estore.dto.auth.response.AuthResponse;
import com.fitlux.estore.model.auth.*;
import com.fitlux.estore.repository.auth.RefreshTokenRepository;
import com.fitlux.estore.repository.auth.RoleRepository;
import com.fitlux.estore.repository.auth.UserRepository;
import com.fitlux.estore.repository.auth.UserRoleRepository;
import com.fitlux.estore.util.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Jwtutil jwtutil;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EncryptionUtil encryptionUtil;

    @Value("${security.jwt.refresh-token.expiration-seconds}")
    private long refreshTokenDurationSeconds;

    @Value("${security.jwt.expiration-seconds}")
    private long accessTokenExpirationSeconds;

    public AuthService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder, 
                       Jwtutil jwtutil, 
                       RoleRepository roleRepository, 
                       UserRoleRepository userRoleRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       EncryptionUtil encryptionUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtutil = jwtutil;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.encryptionUtil = encryptionUtil;
    }

    @Transactional()
    public AuthResponse login(LoginRequest request) {
        logger.info("Attempting login for email: {}", request.email());
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    logger.warn("Login failed: User not found for email: {}", request.email());
                    return new UnauthorizedException(serviceCodeImpl.LOGIN_FAILED);
                });

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            logger.warn("Login failed: Invalid password for email: {}", request.email());
            throw new UnauthorizedException(serviceCodeImpl.LOGIN_FAILED);
        }

        if (refreshTokenRepository.existsByUserAndRevokedFalseAndExpiresAtAfter(user, LocalDateTime.now())) {
            logger.warn("Login prevented: User already logged in: {}", request.email());
            throw new BusinessException(serviceCodeImpl.ALREADY_LOGGED_IN);
        }

        Map<String, Object> claims = new HashMap<>();
        // Prevent ConcurrentModificationException by using detached copies of collections
        Set<UserRole> userRoles = user.getUserRoles() != null 
                ? new HashSet<>(user.getUserRoles()) 
                : new HashSet<>();

        Set<String> permissions = userRoles.stream()
                .filter(ur -> ur.isActive() && ur.getRole() != null)
                .flatMap(ur -> {
                    Role role = ur.getRole();
                    claims.put("role",role.getName());
                    Set<RolePermission> rolePermissions = role.getRolePermissions() != null 
                            ? new HashSet<>(role.getRolePermissions()) 
                            : new HashSet<>();
                    return rolePermissions.stream();
                })
                .filter(rp -> rp.getPermission() != null)
                .map(rp -> rp.getPermission().getPermissionCode().name())
                .collect(Collectors.toSet());


        claims.put("permissions", permissions);

        String accessToken = jwtutil.generateToken(claims, user.getId().toString());
        RefreshToken refreshToken = createRefreshToken(user);

        String encryptedExpiration = encryptionUtil.encrypt(String.valueOf(accessTokenExpirationSeconds));
        logger.info("Login successful for user: {}", user.getEmail());

        return new AuthResponse(accessToken, refreshToken.getToken(), encryptedExpiration);
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        logger.info("Attempting refresh token");
        return refreshTokenRepository.findByTokenAndRevokedFalse(request.refreshToken())
                .map(refreshToken -> {
                    if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                        logger.warn("Refresh token expired");
                        throw new UnauthorizedException(serviceCodeImpl.REFRESH_TOKEN_EXPIRED);
                    }
                    
                    // Rotate refresh token: revoke old one, create new one
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                    
                    User user = refreshToken.getUser();
                    RefreshToken newRefreshToken = createRefreshToken(user);
                    
                    // Generate new access token
                    // Re-fetch permissions to ensure they are up-to-date
                    Set<String> permissions = getPermissionsForUser(user);
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("permissions", permissions);
                    
                    String newAccessToken = jwtutil.generateToken(claims, user.getId().toString());
                    String encryptedExpiration = encryptionUtil.encrypt(String.valueOf(accessTokenExpirationSeconds));

                    logger.info("Refresh token successful for user: {}", user.getEmail());
                    
                    return new AuthResponse(newAccessToken, newRefreshToken.getToken(), encryptedExpiration);
                })
                .orElseThrow(() -> {
                    logger.warn("Invalid refresh token provided");
                    return new UnauthorizedException(serviceCodeImpl.INVALID_REFRESH_TOKEN);
                });
    }

    @Transactional
    public void logoutByUserId(Long userId) {
        logger.info("Attempting logout for userId: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Logout failed: User not found for userId: {}", userId);
                    return new BusinessException(serviceCodeImpl.USER_NOT_FOUND);
                });

        var tokens = refreshTokenRepository.findByUserAndRevokedFalse(user);
        if (tokens.isEmpty()) {
            logger.warn("Logout failed: User not logged in (no active tokens) for userId: {}", userId);
            throw new BusinessException(serviceCodeImpl.USER_NOT_LOGGED_IN);
        }

        tokens.forEach(t -> t.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
        logger.info("Revoked {} refresh tokens for user: {}", tokens.size(), user.getEmail());
        logger.info("Logout by userId completed");
    }
    private RefreshToken createRefreshToken(User user) {
        // Optionally revoke all previous tokens for this user if we want strict single-session policy
        // refreshTokenRepository.deleteByUser(user); 
        
        RefreshToken refreshToken = RefreshToken.create(user, refreshTokenDurationSeconds);
        return refreshTokenRepository.save(refreshToken);
    }

    private Set<String> getPermissionsForUser(User user) {
        // Detached collection handling for safety
        Set<UserRole> userRoles = user.getUserRoles() != null 
                ? new HashSet<>(user.getUserRoles()) 
                : new HashSet<>();

        return userRoles.stream()
                .filter(ur -> ur.isActive() && ur.getRole() != null)
                .flatMap(ur -> {
                    Role role = ur.getRole();
                    Set<RolePermission> rolePermissions = role.getRolePermissions() != null 
                            ? new HashSet<>(role.getRolePermissions()) 
                            : new HashSet<>();
                    return rolePermissions.stream();
                })
                .filter(rp -> rp.getPermission() != null)
                .map(rp -> rp.getPermission().getPermissionCode().name())
                .collect(Collectors.toSet());
    }


    @Transactional
    public void signup(SignupRequest request) {
        logger.info("Attempting signup for email: {}", request.email());

        boolean userExist= userRepository.findByEmail(request.email()).isPresent();
        if (userExist) {
            logger.warn("Signup failed: User already exists for email: {}", request.email());
            throw new BusinessException(serviceCodeImpl.USER_ALREADY_EXISTS);
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        Role customerRole = roleRepository.findByRoleCode(RoleCode.CUSTOMER)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setRoleCode(RoleCode.CUSTOMER);
                    role.setName("Customer");
                    role.setDescription("Default customer role");
                    role.setActive(true);
                    return roleRepository.save(role);
                });

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(customerRole);
        userRole.setAssignedAt(LocalDateTime.now());
        userRole.setActive(true);

        userRoleRepository.save(userRole);
    }
}
