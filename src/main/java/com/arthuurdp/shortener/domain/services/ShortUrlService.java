package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.url.ShortUrl;
import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTOResponse;
import com.arthuurdp.shortener.domain.entities.url.ShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
import com.arthuurdp.shortener.domain.services.exceptions.AccessDeniedException;
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
        User user = authService.getCurrentUser();

        if (user.getRole() == Role.ROLE_USER) {
            return repo.findAllByUserId(user.getId())
                    .stream()
                    .map(entityMapper::toShortUrlDTO)
                    .toList();
        }

        return repo.findAllWithUserId()
                .stream()
                .map(entityMapper::toShortUrlDTO)
                .toList();
    }


    public CreateShortUrlDTOResponse createShortUrl(CreateShortUrlDTO dto) {
        ShortUrl url = new ShortUrl(
                keyGenerator.generate(),
                dto.originalUrl(),
                authService.getCurrentUser()
        );

        repo.save(url);
        return entityMapper.toCreateShortUrlDTOResponse(url);
    }

    @Transactional
    public String getOriginalUrl(String shortKey) {
        ShortUrl url = repo.findByShortKey(shortKey).orElseThrow(() -> new ResourceNotFoundException("Original url not found: " + shortKey));
        url.incrementClicks();
        return url.getOriginalUrl();
    }

    @Transactional
    public void deleteById(Long id) {
        User user = authService.getCurrentUser();
        ShortUrl url = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("URL not found"));

        if (!url.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only delete your own urls");
        }

        repo.deleteById(id);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteInactiveUrls() {
        Instant limit = Instant.now().minus(60, ChronoUnit.DAYS);
        repo.deleteAllInactive(limit);
    }
}
