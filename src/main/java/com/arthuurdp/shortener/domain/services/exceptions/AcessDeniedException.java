package com.arthuurdp.shortener.domain.services.exceptions;

public class AcessDeniedException extends RuntimeException {
    public AcessDeniedException(String message) {
        super(message);
    }
}
