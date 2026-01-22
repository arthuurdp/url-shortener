package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.ShortUrl;
import com.arthuurdp.shortener.domain.entities.dtos.CreateShortUrlDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    void deleteByShortKey(String shortKey);
    Optional<ShortUrl> findByShortKey(String shortKey);
}
