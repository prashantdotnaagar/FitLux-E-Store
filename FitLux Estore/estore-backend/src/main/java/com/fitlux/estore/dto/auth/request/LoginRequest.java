package com.fitlux.estore.dto.auth.request;

public record LoginRequest(
        String email,
        String password
) {}
