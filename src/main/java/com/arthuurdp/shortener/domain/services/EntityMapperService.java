package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.url.ShortUrl;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.entities.url.ShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class EntityMapperService {

    // Learning DTO concepts: transforming entities into DTOs to expose
    // only the required attributes, avoiding returning all database fields in API Endpoints.

    public ShortUrlDTO toShortUrlDTO(ShortUrl shortUrl) {
        return new ShortUrlDTO(
                shortUrl.getShortKey(),
                shortUrl.getOriginalUrl(),
                shortUrl.getExpiresAt()
        );
    }

    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
