package com.arthuurdp.shortener.domain.entities.dtos;

import java.io.Serializable;

public record ShortUrlDTO(String shortKey, String originalUrl) implements Serializable {
}
