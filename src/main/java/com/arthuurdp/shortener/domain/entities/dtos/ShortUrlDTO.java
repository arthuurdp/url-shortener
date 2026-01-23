package com.arthuurdp.shortener.domain.entities.dtos;

import java.io.Serializable;
import java.time.Instant;

public record ShortUrlDTO(String shortKey, String originalUrl, Instant createdAt, Instant expiresAt) implements Serializable {
}
