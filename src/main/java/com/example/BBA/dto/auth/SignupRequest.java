package com.example.BBA.dto.auth;

import jakarta.validation.constraints.*;

public record SignupRequest(
    @NotBlank(message = "Username is required") String username,
    @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
    @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
    String phoneNumber,
    String gender
) {}
