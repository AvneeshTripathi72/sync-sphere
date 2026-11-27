package com.example.drive.service;

import com.example.drive.dto.LoginRequest;
import com.example.drive.dto.RegisterRequest;
import com.example.drive.dto.UpdateUserRequest;
import com.example.drive.dto.UserResponse;
import com.example.drive.entity.UserEntity;
import com.example.drive.exception.BadRequestException;
import com.example.drive.exception.ResourceNotFoundException;
import com.example.drive.exception.UnauthorizedException;
import com.example.drive.repo.UserRepository;
import com.example.drive.util.PasswordHasher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Handles user registration, authentication, and profile management.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse registerUser(RegisterRequest request) {
        if (request == null || isBlank(request.getUsername())
                || isBlank(request.getEmail()) || isBlank(request.getPassword())) {
            throw new BadRequestException("Username, email, and password are required");
        }
        if (userRepository.existsByUsername(request.getUsername().trim())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new BadRequestException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setPassword(PasswordHasher.hash(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setCreatedAt(LocalDateTime.now());

        return UserResponse.fromEntity(userRepository.save(user));
    }

    public UserResponse loginUser(LoginRequest request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            throw new BadRequestException("Username and password are required");
        }

        UserEntity user = userRepository.findByUsername(request.getUsername().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!PasswordHasher.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        user.setLastLogin(LocalDateTime.now());
        return UserResponse.fromEntity(userRepository.save(user));
    }

    public UserResponse getUserById(Long userId) {
        return UserResponse.fromEntity(findUserOrThrow(userId));
    }

    public UserResponse getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return UserResponse.fromEntity(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        UserEntity user = findUserOrThrow(userId);
        if (request == null) {
            return UserResponse.fromEntity(user);
        }
        if (!isBlank(request.getUsername())) {
            user.setUsername(request.getUsername().trim());
        }
        if (!isBlank(request.getEmail())) {
            user.setEmail(request.getEmail().trim());
        }
        if (!isBlank(request.getPassword())) {
            user.setPassword(PasswordHasher.hash(request.getPassword()));
        }
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        return UserResponse.fromEntity(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    private UserEntity findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
