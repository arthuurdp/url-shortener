package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.url.*;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

    public Page<ShortUrlDTO> getAll(int page, int size) {
        User user = authService.getCurrentUser();

        Pageable pageable = PageRequest.of(page, size);
        Page<ShortUrl> urlPage;

        if (user.getRole() == Role.ROLE_USER) {
            urlPage = repo.findAllByUserId(user.getId(), pageable);
        } else {
            urlPage = repo.findAll(pageable);
        }

        return urlPage.map(entityMapper::toShortUrlDTO);
    }

    @Transactional
    public CreateShortUrlResponse createShortUrl(CreateShortUrlRequest dto) {
        User user = authService.getCurrentUser();

        if (dto.customKey() != null && !dto.customKey().isBlank()) {
            if (repo.existsByShortKey(dto.customKey())) {
                throw new DataIntegrityViolationException("Custom key already in use");
            }
        }

        ShortUrl url = new ShortUrl(dto.originalUrl(), user);
        url = repo.save(url);

        String key = (dto.customKey() != null && !dto.customKey().isBlank()) ? dto.customKey() : keyGenerator.encode(url.getId());
        url.setShortKey(key);

        return entityMapper.toCreateShortUrlDTOResponse(url);
    }

    @Transactional
    public String getOriginalUrl(String shortKey) {
        ShortUrl url = repo.findByShortKey(shortKey).orElseThrow(() -> new ResourceNotFoundException("Original url not found"));
        url.incrementClicks();
        return url.getOriginalUrl();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @authService.isUrlOwner(#id)")
    public void deleteById(Long id) {
        ShortUrl url = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("URL not found"));
        repo.deleteById(id);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteInactiveUrls() {
        Instant limit = Instant.now().minus(60, ChronoUnit.DAYS);
        repo.deleteAllInactive(limit);
    }
}
