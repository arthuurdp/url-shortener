package com.arthuurdp.shortener.domain.entities.dtos;

import java.time.Instant;

public record ShortUrlDTO(String shortKey, String originalUrl, Instant expiresAt) {
}
