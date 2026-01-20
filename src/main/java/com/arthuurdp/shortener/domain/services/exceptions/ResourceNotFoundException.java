package com.arthuurdp.shortener.domain.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Object id) {
        super("ID: " + id + " wasn't found.");
    }
}
