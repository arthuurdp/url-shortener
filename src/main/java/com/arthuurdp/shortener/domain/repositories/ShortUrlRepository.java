package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.url.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    void deleteByExpiresAtBefore(Instant now);
    void deleteByShortKey(String shortKey);

    Optional<ShortUrl> findByShortKey(String shortKey);
}
