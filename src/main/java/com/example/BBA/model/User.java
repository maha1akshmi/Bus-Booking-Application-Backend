package com.example.BBA.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String username;
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;
    @Column(length = 10)
    private String gender;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ADMIN','USER') DEFAULT 'USER'")
    private Role role;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Role { ADMIN, USER }

    public User() {}
    public User(Long id, String username, String email, String passwordHash, String phoneNumber, String gender, Role role) { this.id = id; this.username = username; this.email = email; this.passwordHash = passwordHash; this.phoneNumber = phoneNumber; this.gender = gender; this.role = role; }

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); if (this.role == null) this.role = Role.USER; }
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private String username, email, passwordHash, phoneNumber, gender;
        private Role role;
        public UserBuilder username(String v) { this.username = v; return this; }
        public UserBuilder email(String v) { this.email = v; return this; }
        public UserBuilder passwordHash(String v) { this.passwordHash = v; return this; }
        public UserBuilder phoneNumber(String v) { this.phoneNumber = v; return this; }
        public UserBuilder gender(String v) { this.gender = v; return this; }
        public UserBuilder role(Role v) { this.role = v; return this; }
        public User build() { return new User(null, username, email, passwordHash, phoneNumber, gender, role); }
    }
}
