package com.arthuurdp.shortener.domain.entities.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// login
public record AuthDTO(
        @NotBlank(message = "E-mail is required")
        @Email
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password has to be at least 6 chars")
        String password) {
}
