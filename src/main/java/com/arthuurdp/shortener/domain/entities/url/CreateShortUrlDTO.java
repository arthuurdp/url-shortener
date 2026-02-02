package com.arthuurdp.shortener.domain.entities.url;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Schema(description = "Request to create a short URL")
public record CreateShortUrlDTO(
        @NotBlank(message = "Please enter a valid URL")
        @URL
        String originalUrl) {
}
