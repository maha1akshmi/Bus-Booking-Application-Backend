package com.example.BBA.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiration = expiration;
    }

    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder().subject(email).claims(Map.of("userId", userId, "role", role)).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expiration)).signWith(key).compact();
    }

    public String generateResetToken(String email) {
        return Jwts.builder().subject(email).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 3600000)).signWith(key).compact();
    }

    public Claims extractClaims(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }

    public String extractEmail(String token) { return extractClaims(token).getSubject(); }

    public Long extractUserId(String token) { return ((Number) extractClaims(token).get("userId")).longValue(); }

    public String extractRole(String token) { return (String) extractClaims(token).get("role"); }

    public boolean isTokenValid(String token) {
        try { return !extractClaims(token).getExpiration().before(new Date()); } catch (JwtException e) { return false; }
    }
}
