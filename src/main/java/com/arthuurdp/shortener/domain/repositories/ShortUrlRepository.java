package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.ShortUrl;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    // some database operations, such as delete, require @transactional, due to the method being created manually.
    @Transactional
    void deleteByExpiresAtBefore(Instant now);
    @Transactional
    void deleteByShortKey(String shortKey);

    Optional<ShortUrl> findByShortKey(String shortKey);
}
