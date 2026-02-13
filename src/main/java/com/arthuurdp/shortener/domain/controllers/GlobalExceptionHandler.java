package com.arthuurdp.shortener.domain.controllers;

import com.arthuurdp.shortener.domain.services.exceptions.AccessDeniedException;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import com.arthuurdp.shortener.domain.services.exceptions.StandardError;
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

@RestControllerAdvice
public class GlobalExceptionHandler {

    // when nothing is found in the database
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException e) {
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandardError(
               Instant.now(),
               HttpStatus.NOT_FOUND.value(),
               "Not Found",
               e.getMessage()
       ));
   }

   // doesn't have access
   @ExceptionHandler(AccessDeniedException.class)
   public ResponseEntity<StandardError> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new StandardError(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                e.getMessage()
        ));
   }

   // handling spring exception
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<StandardError> handleSpringAccessDenied(org.springframework.security.access.AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new StandardError(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                e.getMessage()
        ));
    }

    // if the short key was already generated
   @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new StandardError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Short key already exists"
        ));
   }

   // badly written
   @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Invalid syntax"
        ));
   }

   // wrong method
   @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
   public ResponseEntity<StandardError> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new StandardError(
                Instant.now(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                "Http method not supported for this endpoint"
        ));
   }

   // invalid url sent
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                e.getBindingResult().getFieldError().getDefaultMessage()
        ));
    }

    // the endpoint doesn't exist
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<StandardError> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandardError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "Invalid path: the endpoint doesn't exist"
        ));
    }

    // invalid data type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Not Found",
                "Invalid data type"
        ));
    }

    // handling unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardError(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later"
        ));
    }
}
