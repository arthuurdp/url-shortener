package com.arthuurdp.shortener.domain.entities.user;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.url.ShortUrl;

import java.util.List;

public record UserResponseDTO(Long id, String firstName, String lastName, String email, Role role, List<ShortUrl> urls) {
}
