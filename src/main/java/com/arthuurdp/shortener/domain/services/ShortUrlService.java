package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.ShortUrl;
import com.arthuurdp.shortener.domain.entities.dtos.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.dtos.ShortUrlDTO;
import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.arthuurdp.shortener.domain.services.ShortKeyGenerator.generate;

@Service
public class ShortUrlService {
    private ShortUrlRepository repo;
    private EntityMapper entityMapper;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, EntityMapper entityMapper) {
        this.repo = shortUrlRepository;
        this.entityMapper = entityMapper;
    }

    public ShortUrlDTO insert(CreateShortUrlDTO dto) {
        ShortUrl url = new ShortUrl();
        url.setOriginalUrl(dto.originalUrl());
        url.setShortKey(generate());
        url.setCreatedAt(Instant.now());
        url.setExpiresAt(url.getCreatedAt().plus(7, ChronoUnit.DAYS));

        repo.save(url);
        return entityMapper.toShortUrlDTO(url);
    }

    public String getOriginalUrl(String shortKey) {
        return repo.findByShortKey(shortKey)
                .map(ShortUrl::getOriginalUrl)
                .orElseThrow(() -> new ResourceNotFoundException("Original url not found: " + shortKey));
    }

    public void deleteByShortKey(String shortKey) {
        repo.deleteByShortKey(shortKey);
    }

    public void deleteByExpiresAtBefore() {
        repo.deleteByExpiresAtBefore(Instant.now());
    }
}
