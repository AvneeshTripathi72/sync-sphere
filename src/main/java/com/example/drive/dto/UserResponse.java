package com.example.drive.dto;

import com.example.drive.entity.UserEntity;

import java.time.LocalDateTime;

/**
 * Safe user representation returned by API endpoints without exposing passwords.
 */
public class UserResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String fullName;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastLogin;

    public UserResponse(Long id, String username, String email, String fullName,
                        LocalDateTime createdAt, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    public static UserResponse fromEntity(UserEntity entity) {
        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFullName(),
                entity.getCreatedAt(),
                entity.getLastLogin()
        );
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
}
