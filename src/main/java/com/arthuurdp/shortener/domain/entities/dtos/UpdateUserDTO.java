package com.arthuurdp.shortener.domain.entities.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Email(message = "E-mail is required")
        String email,

        @NotBlank(message = "Please enter a valid password")
        @Size(min = 6, message = "The password has to be at least 6 chars")
        String password) {
}
