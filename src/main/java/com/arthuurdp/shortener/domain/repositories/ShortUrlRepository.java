package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.url.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    void deleteByExpiresAtBefore(Instant now);
    void deleteByShortKey(String shortKey);
    List<ShortUrl> findAllByUserId(Long id);

    Optional<ShortUrl> findByShortKey(String shortKey);
}
