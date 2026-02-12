package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.url.ShortUrl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    @EntityGraph(attributePaths = {"user"})
    Page<ShortUrl> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<ShortUrl> findAllByUserId(Long userId, Pageable pageable);

    @Modifying
    @Query("DELETE from ShortUrl s WHERE s.lastClickedAt < :limit")
    void deleteAllInactive(@Param("limit") Instant limit);

    @EntityGraph(attributePaths = {"user"})
    Optional<ShortUrl> findByShortKey(String shortKey);
}
