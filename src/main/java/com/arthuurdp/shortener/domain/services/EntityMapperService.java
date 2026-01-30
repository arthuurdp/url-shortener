package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.url.ShortUrl;
import com.arthuurdp.shortener.domain.entities.url.ShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTOResponse;
import com.arthuurdp.shortener.domain.entities.user.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EntityMapperService {

    // Learning DTO concepts: transforming entities into DTOs to expose
    // only the required attributes, avoiding returning all database fields in API Endpoints.

    public CreateShortUrlDTOResponse toCreateShortUrlDTOResponse(ShortUrl shortUrl) {
        return new CreateShortUrlDTOResponse(
                shortUrl.getShortKey()
        );
    }

    public ShortUrlDTO toShortUrlDTO(ShortUrl shortUrl) {
        return new ShortUrlDTO(
                shortUrl.getId(),
                shortUrl.getShortKey(),
                shortUrl.getOriginalUrl(),
                shortUrl.getCreatedAt(),
                shortUrl.getExpiresAt(),
                shortUrl.getLastClickedAt(),
                shortUrl.getClicks(),
                shortUrl.getUser().getFirstName()
        );
    }

    public UserResponseDTO toUserDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getShortUrls().stream().map(
                        this::toShortUrlDTO
                ).toList()
        );
    }
}
