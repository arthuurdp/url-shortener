package com.arthuurdp.shortener.domain.entities.url;

import java.time.Instant;

public record ShortUrlDTO(Long id, String shortKey, String originalUrl, Instant createdAt, Instant expiresAt, Instant lastClickedAt, Integer clicks, String createdBy) {
}
