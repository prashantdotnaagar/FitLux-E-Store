package com.fitlux.estore.dto.auth.request;

public record SignupRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone
) {}
