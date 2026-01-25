package com.arthuurdp.shortener.domain.entities.dtos;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank
        @Email(message = "Please enter a valid e-mail")
        String email,

        @NotBlank(message = "Please enter a valid password")
        @Size(min = 6, message = "The password has to be at least 6 chars")
        String password,

        @NotNull(message = "Role is required")
        Role role
) {
}
