package com.arthuurdp.shortener.domain.entities.user;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank
        @Email(message = "E-mail is required")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "The password has to be at least 6 chars")
        String password,

        @NotNull(message = "Role is required")
        Role role
) {
}
