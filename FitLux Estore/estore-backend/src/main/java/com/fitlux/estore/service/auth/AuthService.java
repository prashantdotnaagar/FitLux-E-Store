package com.fitlux.estore.service.auth;

import com.fitlux.estore.common.exception.BusinessException;
import com.fitlux.estore.common.exception.UnauthorizedException;
import com.fitlux.estore.config.Jwtutil;
import com.fitlux.estore.constants.serviceCodes.enums.RoleCode;
import com.fitlux.estore.constants.serviceCodes.enums.UserStatus;
import com.fitlux.estore.constants.serviceCodes.enums.serviceCodeImpl;
import com.fitlux.estore.dto.auth.request.LoginRequest;
import com.fitlux.estore.dto.auth.request.SignupRequest;
import com.fitlux.estore.dto.auth.response.AuthResponse;
import com.fitlux.estore.model.auth.Role;
import com.fitlux.estore.model.auth.RolePermission;
import com.fitlux.estore.model.auth.User;
import com.fitlux.estore.model.auth.UserRole;
import com.fitlux.estore.repository.auth.RoleRepository;
import com.fitlux.estore.repository.auth.UserRepository;
import com.fitlux.estore.repository.auth.UserRoleRepository;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Jwtutil jwtutil;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, Jwtutil jwtutil, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtutil = jwtutil;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException(serviceCodeImpl.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException(serviceCodeImpl.LOGIN_FAILED);
        }

        // Prevent ConcurrentModificationException by using detached copies of collections
        Set<UserRole> userRoles = user.getUserRoles() != null 
                ? new HashSet<>(user.getUserRoles()) 
                : new HashSet<>();

        Set<String> permissions = userRoles.stream()
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

        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", permissions);

        String token = jwtutil.generateToken(claims, user.getId().toString());

        // Assuming 60 minutes as expiration, but better to get it from properties or JwtUtil if exposed
        // JwtUtil uses expirationSeconds property.
        // For now hardcoding 60 as per original controller code.
        return new AuthResponse(token, 60);
    }


    @Transactional
    public void signup(SignupRequest request) {

        boolean userExist= userRepository.findByEmail(request.email()).isPresent();
        if (userExist) {
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
