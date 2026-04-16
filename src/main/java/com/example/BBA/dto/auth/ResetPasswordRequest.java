package com.example.BBA.dto.auth;

import jakarta.validation.constraints.*;

public record ResetPasswordRequest(
    @NotBlank(message = "Token is required") String token,
    @NotBlank(message = "New password is required") @Size(min = 6, message = "Password must be at least 6 characters") String newPassword
) {}
