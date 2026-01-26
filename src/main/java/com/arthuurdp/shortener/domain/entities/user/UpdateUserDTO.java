package com.arthuurdp.shortener.domain.entities.user;

import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        String firstName,

        String lastName,

        String email,

        @Size(min = 6, message = "The password has to be at least 6 chars")
        String password) {
}
