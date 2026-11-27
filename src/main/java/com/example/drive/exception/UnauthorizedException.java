package com.example.drive.exception;

/**
 * Thrown when authentication credentials are invalid.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
