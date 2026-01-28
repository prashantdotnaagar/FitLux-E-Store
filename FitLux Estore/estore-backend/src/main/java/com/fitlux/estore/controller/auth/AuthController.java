package com.fitlux.estore.controller.auth;

import com.fitlux.estore.constants.ApiResponse;
import com.fitlux.estore.constants.serviceCodes.enums.serviceCodeImpl;
import com.fitlux.estore.dto.auth.request.LoginRequest;
import com.fitlux.estore.dto.auth.request.RefreshTokenRequest;
import com.fitlux.estore.dto.auth.request.SignupRequest;
import com.fitlux.estore.dto.auth.response.AuthResponse;
import com.fitlux.estore.service.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        logger.info("Received login request");
        AuthResponse response = authService.login(request);
        logger.info("Login request processed successfully");
        return ResponseEntity.ok(ApiResponse.success(serviceCodeImpl.LOGIN_SUCCESS, response));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(
            @RequestBody SignupRequest request
    ) {
        logger.info("Received signup request");
        authService.signup(request);
        logger.info("Signup request processed successfully");
        return ResponseEntity.ok(
                ApiResponse.success(serviceCodeImpl.USER_CREATED, null)
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        logger.info("Received refresh token request");
        AuthResponse response = authService.refreshToken(request);
        logger.info("Refresh token request processed successfully");
        return ResponseEntity.ok(ApiResponse.success(serviceCodeImpl.TOKEN_REFRESH_SUCCESS, response));
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<ApiResponse<Void>> logoutByUserId(@PathVariable Long userId) {
        logger.info("Received logout request for userId: {}", userId);
        authService.logoutByUserId(userId);
        logger.info("Logout processed successfully for userId: {}", userId);
        return ResponseEntity.ok(ApiResponse.success(serviceCodeImpl.LOGOUT_SUCCESS, null));
    }
}
