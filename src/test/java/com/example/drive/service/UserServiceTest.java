package com.example.drive.service;

import com.example.drive.dto.LoginRequest;
import com.example.drive.dto.RegisterRequest;
import com.example.drive.dto.UserResponse;
import com.example.drive.entity.UserEntity;
import com.example.drive.exception.BadRequestException;
import com.example.drive.exception.UnauthorizedException;
import com.example.drive.repo.UserRepository;
import com.example.drive.util.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void registerUser_hashesPasswordAndReturnsSafeDto() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("alice");
        req.setEmail("alice@example.com");
        req.setPassword("secret123");
        req.setFullName("Alice");

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> {
            UserEntity saved = inv.getArgument(0);
            saved.setId(1L);
            assertNotEquals("secret123", saved.getPassword());
            return saved;
        });

        UserResponse response = userService.registerUser(req);
        assertEquals(1L, response.getId());
        assertEquals("alice", response.getUsername());
        assertEquals("alice@example.com", response.getEmail());
    }

    @Test
    void registerUser_rejectsDuplicateUsername() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("alice");
        req.setEmail("alice@example.com");
        req.setPassword("secret123");

        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.registerUser(req));
    }

    @Test
    void loginUser_rejectsInvalidPassword() {
        UserEntity stored = new UserEntity();
        stored.setId(1L);
        stored.setUsername("alice");
        stored.setPassword(PasswordHasher.hash("correct-pass"));

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(stored));

        LoginRequest req = new LoginRequest();
        req.setUsername("alice");
        req.setPassword("wrong-pass");

        assertThrows(UnauthorizedException.class, () -> userService.loginUser(req));
    }
}
