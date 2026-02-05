package com.arthuurdp.shortener.domain.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super("User not found");
    }
}
