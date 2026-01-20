package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.ShortUrl;
import com.arthuurdp.shortener.domain.entities.dtos.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.dtos.ShortUrlDTO;
import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.arthuurdp.shortener.domain.services.ShortKeyGenerator.generate;

@Service
public class ShortUrlService {
    private ShortUrlRepository repo;
    private EntityMapper entityMapper;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, EntityMapper entityMapper) {
        this.repo = shortUrlRepository;
        this.entityMapper = entityMapper;
    }

    public ShortUrlDTO findById(Long id) {
        return repo.findById(id).stream().map(entityMapper::toShortUrlDTO).findFirst().orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<ShortUrlDTO> findAll() {
        return repo.findAll().stream().map(entityMapper::toShortUrlDTO).toList();
    }

    public ShortUrlDTO insert(CreateShortUrlDTO dto) {
        ShortUrl url = new ShortUrl();
        url.setOriginalUrl(dto.originalUrl());
        url.setShortKey(generate());

        repo.save(url);
        return entityMapper.toShortUrlDTO(url);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
