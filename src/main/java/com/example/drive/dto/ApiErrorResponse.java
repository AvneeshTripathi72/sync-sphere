package com.example.drive.dto;

import java.time.Instant;

/**
 * Standard error payload returned by {@link com.example.drive.exception.GlobalExceptionHandler}.
 */
public class ApiErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final Instant timestamp;

    public ApiErrorResponse(int status, String message) {
        this.status = status;
        this.error = message;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
