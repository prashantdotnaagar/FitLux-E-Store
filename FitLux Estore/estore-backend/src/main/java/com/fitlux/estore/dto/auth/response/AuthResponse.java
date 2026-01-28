package com.fitlux.estore.dto.auth.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String expiresIn
) {}
