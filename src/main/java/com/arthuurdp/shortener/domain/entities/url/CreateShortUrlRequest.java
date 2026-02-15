package com.arthuurdp.shortener.domain.entities.url;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlRequest(
        @NotBlank(message = "Please enter a valid URL")
        @URL(message = "Invalid URL format")
        String originalUrl,

        @Size(min = 3, max = 10, message = "Custom key must be between 3 and 10 characters")
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]+$",
                message = "Only letters, numbers, hyphens and underscores allowed"
        )
        String customKey) {}

