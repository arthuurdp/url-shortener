package com.arthuurdp.shortener.domain.entities.url;

import java.time.Instant;

public record ShortUrlDTO(String shortKey, String originalUrl, Instant expiresAt) {
}
