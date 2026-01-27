package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.url.ShortUrl;
import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTOResponse;
import com.arthuurdp.shortener.domain.entities.url.ShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
import com.arthuurdp.shortener.domain.services.exceptions.AcessDeniedException;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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
    private final AuthService authService;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, EntityMapperService entityMapper, ShortKeyGeneratorService keyGenerator, AuthService authService) {
        this.repo = shortUrlRepository;
        this.entityMapper = entityMapper;
        this.keyGenerator = keyGenerator;
        this.authService = authService;
    }

    public List<ShortUrlDTO> getAll() {
        return repo.findAll().stream().map(entityMapper::toShortUrlDTO).toList();
    }

    public CreateShortUrlDTOResponse createShortUrl(CreateShortUrlDTO dto) {
        User user = authService.getCurrentUser();
        String shortKey = keyGenerator.generate();

        ShortUrl url = new ShortUrl();
        url.setOriginalUrl(dto.originalUrl());
        url.setShortKey(shortKey);
        url.setCreatedAt(Instant.now());
        url.setExpiresAt(url.getCreatedAt().plus(7, ChronoUnit.DAYS));
        url.setUser(user);

        repo.save(url);
        return entityMapper.toCreateShortUrlDTOResponse(url);
    }

    public String getOriginalUrl(String shortKey) {
        return repo.findByShortKey(shortKey)
                .map(ShortUrl::getOriginalUrl)
                .orElseThrow(() -> new ResourceNotFoundException("Original url not found: " + shortKey));
    }

    public List<ShortUrlDTO> findAllByUserId(Long id) {
        return repo.findAllByUserId(id).stream().map(entityMapper::toShortUrlDTO).toList();
    }

    @Transactional
    public void deleteById(Long id) {
        User user = authService.getCurrentUser();
        ShortUrl url = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("URL not found"));

        if (!url.getUser().getId().equals(user.getId())) {
            throw new AcessDeniedException("You can only delete your own urls");
        }

        repo.deleteById(id);
    }

    // every 1 hour this method executes
    @Scheduled(cron = "0 0 * * * *")
    public void deleteByExpiresAtBefore() {
        repo.deleteByExpiresAtBefore(Instant.now());
    }
}
