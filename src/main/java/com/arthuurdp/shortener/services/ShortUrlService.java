package com.arthuurdp.shortener.services;

import com.arthuurdp.shortener.entities.ShortUrl;
import com.arthuurdp.shortener.repositories.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortUrlService {
    @Autowired
    private ShortUrlRepository shortUrlRepository;

    public List<ShortUrl> findAllPublicShortUrls() {
        return shortUrlRepository.findPublicShortUrls();
    }
}
