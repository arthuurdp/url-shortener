package com.arthuurdp.shortener.domain.entities.user;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        String firstName,

        String lastName,

        @Email
        String email,

        @Size(min = 6, message = "The password has to be at least 6 chars")
        String password,

        Role role
) {
}
