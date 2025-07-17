package com.example.drive.services;

import com.example.drive.entity.UserEntity;
import com.example.drive.repo.userRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final userRepository userRepository;

    public UserService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity registerUser(UserEntity user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Set creation time
        user.setCreatedAt(LocalDateTime.now());
        
        // Save user to database
        return userRepository.save(user);
    }
    
    public UserEntity loginUser(String username, String password) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        UserEntity user = userOptional.get();
        
        // In a real application, you would use a password encoder
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        
        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
    
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    
    public UserEntity updateUser(Long userId, UserEntity userDetails) {
        UserEntity user = getUserById(userId);
        
        // Update user details
        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }
        
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        
        if (userDetails.getPassword() != null) {
            user.setPassword(userDetails.getPassword());
        }
        
        if (userDetails.getFullName() != null) {
            user.setFullName(userDetails.getFullName());
        }
        
        return userRepository.save(user);
    }
    
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}