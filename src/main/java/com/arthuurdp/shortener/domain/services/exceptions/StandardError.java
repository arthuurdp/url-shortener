package com.arthuurdp.shortener.domain.services.exceptions;

import java.time.Instant;

public record StandardError(Instant timestamp, Integer status, String error, String message) {
}
