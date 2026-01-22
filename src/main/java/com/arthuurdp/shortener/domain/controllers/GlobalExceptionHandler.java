package com.arthuurdp.shortener.domain.controllers;

import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // local exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException e) {
       Map<String, Object> response = Map.of(
               "timestamp", Instant.now(),
               "status", HttpStatus.NOT_FOUND.value(),
               "error", "Not Found",
               "message", e.getMessage()
       );
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
   }

   // if the short key was already generated
   @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflict",
                "message", "Short key already exists"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
   }

   // badly written
   @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", "Invalid syntax"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
   }

   // wrong method
   @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
   public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.METHOD_NOT_ALLOWED.value(),
                "error", "Method Not Allowed",
                "message", "Http method not supported for this endpoint"
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
   }

   // invalid url sent
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", message
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    // endpoint doesn't exist
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(NoResourceFoundException e) {
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", "Invalid path: the endpoint doesn't exist"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // invalid data type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Not Found",
                "message", "Invalid data type"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // handling unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Internal Server Error",
                "message", "An unexpected error occurred. Please try again later"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
