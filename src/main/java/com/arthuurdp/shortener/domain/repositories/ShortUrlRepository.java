package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.url.ShortUrl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    @Query("SELECT s FROM ShortUrl s JOIN FETCH s.user")
    List<ShortUrl> findAllWithUserId();
    @Query("SELECT s FROM ShortUrl s JOIN FETCH s.user WHERE s.id = :id")
    ShortUrl findByIdWithUser(Long id);

    void deleteByExpiresAtBefore(Instant now);
    Optional<ShortUrl> findByShortKey(String shortKey);
}
