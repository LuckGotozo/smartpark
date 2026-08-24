package com.smartpark.smartpark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException exception) {

        Map<String, Object> error = new LinkedHashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", exception.getStatusCode().value());
        error.put("error", exception.getStatusCode().toString());
        error.put("message", exception.getReason());

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException exception) {

        Map<String, String> validationErrors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                        validationErrors.put(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                );

        Map<String, Object> error = new LinkedHashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Validation Error");
        error.put("errors", validationErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}