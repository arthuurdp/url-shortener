package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.ShortUrl;
import com.arthuurdp.shortener.domain.entities.dtos.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.dtos.ShortUrlDTO;
import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ShortUrlService {
    private final ShortUrlRepository repo;
    private final EntityMapperService entityMapper;
    private final ShortKeyGeneratorService keyGenerator;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, EntityMapperService entityMapper, ShortKeyGeneratorService keyGenerator) {
        this.repo = shortUrlRepository;
        this.entityMapper = entityMapper;
        this.keyGenerator = keyGenerator;
    }

    public List<ShortUrlDTO> getAll() {
        return repo.findAll().stream().map(entityMapper::toShortUrlDTO).toList();
    }

    public ShortUrlDTO createShortUrl(CreateShortUrlDTO dto) {
        String shortKey = keyGenerator.generate();

        ShortUrl url = new ShortUrl();
        url.setOriginalUrl(dto.originalUrl());
        url.setShortKey(shortKey);
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

    // every 1 hour this method executes
    @Scheduled(cron = "0 0 * * * *")
    public void deleteByExpiresAtBefore() {
        repo.deleteByExpiresAtBefore(Instant.now());
    }
}
