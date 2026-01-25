package com.arthuurdp.shortener.domain.entities.dtos;

import com.arthuurdp.shortener.domain.entities.enums.Role;

public record UserDTO(Long id, String firstName, String lastName, String email, Role role) {
}
