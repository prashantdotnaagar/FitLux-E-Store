package com.fitlux.estore.controller.auth;

import com.fitlux.estore.constants.ApiResponse;
import com.fitlux.estore.constants.serviceCodes.enums.serviceCodeImpl;
import com.fitlux.estore.dto.auth.request.LoginRequest;
import com.fitlux.estore.dto.auth.request.SignupRequest;
import com.fitlux.estore.dto.auth.response.AuthResponse;
import com.fitlux.estore.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(serviceCodeImpl.LOGIN_SUCCESS, response));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(
            @RequestBody SignupRequest request
    ) {
        authService.signup(request);
        return ResponseEntity.ok(
                ApiResponse.success(serviceCodeImpl.USER_CREATED, null)
        );
    }
}
