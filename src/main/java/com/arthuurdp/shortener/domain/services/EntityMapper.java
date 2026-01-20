package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.ShortUrl;
import com.arthuurdp.shortener.domain.entities.dtos.ShortUrlDTO;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    // Learning DTO concepts: transforming entities into DTOs to expose
    // only the required attributes, avoiding returning all database fields in API Endpoints.

    public ShortUrlDTO toShortUrlDTO(ShortUrl shortUrl) {
        return new ShortUrlDTO(
                shortUrl.getId(),
                shortUrl.getShortKey(),
                shortUrl.getOriginalUrl(),
                shortUrl.getCreatedAt()
        );
    }
}
