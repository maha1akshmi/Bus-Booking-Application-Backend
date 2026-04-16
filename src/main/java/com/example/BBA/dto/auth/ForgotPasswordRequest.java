package com.example.BBA.dto.auth;

import jakarta.validation.constraints.*;

public record ForgotPasswordRequest(
    @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email
) {}
