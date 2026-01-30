package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.url.ShortUrl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    @Query("SELECT s FROM ShortUrl s JOIN FETCH s.user")
    List<ShortUrl> findAllWithUserId();

    @Query("SELECT s FROM ShortUrl s JOIN FETCH s.user WHERE s.user.id = :id")
    List<ShortUrl> findAllByUserId(Long id);

    @Modifying
    @Query("DELETE from ShortUrl s WHERE s.lastClickedAt < :limit")
    void deleteAllInactive(@Param("limit") Instant limit);

    Optional<ShortUrl> findByShortKey(String shortKey);
}
