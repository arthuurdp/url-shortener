package com.arthuurdp.shortener.domain.entities.user;

import com.arthuurdp.shortener.domain.entities.enums.Role;

public record UserWithoutUrlsDTO(Long id, String firstName, String lastName, String email, Role role) {
}
