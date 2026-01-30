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

    public List<ShortUrlDTO> getAllAdmin() {
        return repo.findAllWithUserId().stream().map(entityMapper::toShortUrlDTO).toList();
    }

    public ShortUrlDTO findById(Long id) {
        ShortUrl url = repo.findByIdWithUser(id);
        return entityMapper.toShortUrlDTO(url);
    }

    public CreateShortUrlDTOResponse createShortUrl(CreateShortUrlDTO dto) {
        ShortUrl url = new ShortUrl(
                keyGenerator.generate(),
                dto.originalUrl(),
                Instant.now().plus(7, ChronoUnit.DAYS),
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
