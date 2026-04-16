package com.example.BBA.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", 404, "message", ex.getMessage(), "timestamp", LocalDateTime.now().toString()));
    }

    @ExceptionHandler(SeatLockException.class)
    public ResponseEntity<Map<String, Object>> handleSeatLock(SeatLockException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status", 409, "message", ex.getMessage(), "timestamp", LocalDateTime.now().toString()));
    }

    @ExceptionHandler(DoubleBookingException.class)
    public ResponseEntity<Map<String, Object>> handleDoubleBooking(DoubleBookingException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status", 409, "message", ex.getMessage(), "timestamp", LocalDateTime.now().toString()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", 400, "message", ex.getMessage(), "timestamp", LocalDateTime.now().toString()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", 400, "message", errors, "timestamp", LocalDateTime.now().toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", 500, "message", ex.getMessage() != null ? ex.getMessage() : "Internal server error", "timestamp", LocalDateTime.now().toString()));
    }
}
