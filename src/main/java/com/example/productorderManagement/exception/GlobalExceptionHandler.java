package com.example.productorderManagement.exception;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomBadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleCustomBadCredentials(CustomBadCredentialsException ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("status", HttpStatus.UNAUTHORIZED.value());
    error.put("error", "Unauthorized");
    error.put("message", ex.getMessage());
    error.put("timestamp", LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorized(UnauthorizedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("Something went wrong: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}