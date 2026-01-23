package com.fitlux.estore.dto.auth.response;

public record AuthResponse(
        String token,
        long expiresInSeconds
) {}
