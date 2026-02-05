package com.arthuurdp.shortener.domain.entities.user;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.url.ShortUrlDTO;

import java.util.List;

public record UserWithUrlsDTO(Long id, String firstName, String lastName, String email, Role role, List<ShortUrlDTO> urls) {
}
