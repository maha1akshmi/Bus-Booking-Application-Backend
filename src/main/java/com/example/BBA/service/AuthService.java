package com.example.BBA.service;

import com.example.BBA.config.JwtUtil;
import com.example.BBA.dto.auth.*;
import com.example.BBA.exception.ResourceNotFoundException;
import com.example.BBA.model.User;
import com.example.BBA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JavaMailSender mailSender;

    @Value("${google.client.id}")
    private String googleClientId;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, JavaMailSender mailSender) {
        this.userRepository = userRepository; this.passwordEncoder = passwordEncoder; this.jwtUtil = jwtUtil; this.mailSender = mailSender;
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) throw new IllegalArgumentException("Email already registered");
        User user = User.builder().username(request.username()).email(request.email()).passwordHash(passwordEncoder.encode(request.password())).phoneNumber(request.phoneNumber()).gender(request.gender()).role(User.Role.USER).build();
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.email()));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) throw new IllegalArgumentException("Invalid password");
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getUsername(), user.getRole().name());
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public AuthResponse googleAuth(GoogleAuthRequest request) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> payload = restTemplate.getForObject("https://oauth2.googleapis.com/tokeninfo?id_token=" + request.idToken(), Map.class);
            if (payload == null || !googleClientId.equals(payload.get("aud"))) throw new IllegalArgumentException("Invalid Google ID token");
            String email = (String) payload.get("email");
            String name = (String) payload.get("name");
            User user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(User.builder().username(name != null ? name : email).email(email).role(User.Role.USER).build()));
            String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
            return new AuthResponse(token, user.getId(), user.getEmail(), user.getUsername(), user.getRole().name());
        } catch (Exception e) { throw new IllegalArgumentException("Google authentication failed: " + e.getMessage()); }
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.email()));
        String resetToken = jwtUtil.generateResetToken(user.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Bus Booking - Password Reset");
        message.setText("Your password reset token: " + resetToken + "\n\nThis token expires in 1 hour.");
        mailSender.send(message);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (!jwtUtil.isTokenValid(request.token())) throw new IllegalArgumentException("Invalid or expired reset token");
        String email = jwtUtil.extractEmail(request.token());
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
