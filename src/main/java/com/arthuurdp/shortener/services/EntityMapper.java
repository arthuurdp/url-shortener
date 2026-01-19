package com.arthuurdp.shortener.services;

import com.arthuurdp.shortener.entities.ShortUrl;
import com.arthuurdp.shortener.entities.User;
import com.arthuurdp.shortener.entities.models.ShortUrlDTO;
import com.arthuurdp.shortener.entities.models.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(),  user.getName(), user.getEmail());
    }

    public ShortUrlDTO toShortUrlDTO(ShortUrl shortUrl) {
        UserDTO userDTO = null;
        if (shortUrl.getCreatedBy() != null) {
            userDTO = toUserDTO(shortUrl.getCreatedBy());
        }

        return new ShortUrlDTO(
                shortUrl.getId(),
                shortUrl.getShortKey(),
                shortUrl.getOriginalUrl(),
                shortUrl.getIsPrivate(),
                shortUrl.getExpiresAt(),
                userDTO,
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt()
        );
    }
}
