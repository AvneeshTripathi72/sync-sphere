package com.example.drive.repo;

import com.example.drive.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<UserEntity, Long> {
    
    // Custom method to find user by username
    Optional<UserEntity> findByUsername(String username);
    
    // Custom method to find user by email
    Optional<UserEntity> findByEmail(String email);
    
    // Custom method to check if username exists
    boolean existsByUsername(String username);
    
    // Custom method to check if email exists
    boolean existsByEmail(String email);
}