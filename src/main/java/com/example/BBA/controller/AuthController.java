package com.example.BBA.controller;

import com.example.BBA.dto.auth.*;
import com.example.BBA.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication & Authorization endpoints")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) { return ResponseEntity.ok(authService.signup(request)); }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    @ApiResponse(responseCode = "200", description = "Login successful")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) { return ResponseEntity.ok(authService.login(request)); }

    @PostMapping("/google")
    @Operation(summary = "Login/Register with Google OAuth")
    @ApiResponse(responseCode = "200", description = "Google auth successful")
    public ResponseEntity<AuthResponse> googleAuth(@Valid @RequestBody GoogleAuthRequest request) { return ResponseEntity.ok(authService.googleAuth(request)); }

    @PostMapping("/forgot-password")
    @Operation(summary = "Send password reset email")
    @ApiResponse(responseCode = "200", description = "Reset email sent")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) { authService.forgotPassword(request); return ResponseEntity.ok(Map.of("message", "Password reset email sent successfully")); }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password with token")
    @ApiResponse(responseCode = "200", description = "Password reset successful")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) { authService.resetPassword(request); return ResponseEntity.ok(Map.of("message", "Password reset successfully")); }
}
