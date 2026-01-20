package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
}
