package com.arthuurdp.shortener.domain.entities.dtos;

import java.io.Serializable;
import java.time.Instant;

public record ShortUrlDTO(Long id, String shortKey, String originalUrl, Instant createdAt) implements Serializable {
}
