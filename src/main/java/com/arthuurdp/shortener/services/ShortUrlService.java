package com.arthuurdp.shortener.services;

import com.arthuurdp.shortener.entities.ShortUrl;
import com.arthuurdp.shortener.entities.models.ShortUrlDTO;
import com.arthuurdp.shortener.repositories.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortUrlService {
    private ShortUrlRepository shortUrlRepository;
    private EntityMapper entityMapper;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, EntityMapper entityMapper) {
        this.shortUrlRepository = shortUrlRepository;
        this.entityMapper = entityMapper;
    }

    public List<ShortUrlDTO> findAllPublicShortUrls() {

        return shortUrlRepository.findPublicShortUrls().stream().map(entityMapper::toShortUrlDTO).toList();
    }
}
