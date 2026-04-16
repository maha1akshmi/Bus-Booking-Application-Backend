package com.example.BBA.dto.auth;

public record AuthResponse(String token, Long userId, String email, String username, String role) {}
