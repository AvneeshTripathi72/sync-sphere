package com.example.drive.exception;

/**
 * Thrown when a requested entity or file resource does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
