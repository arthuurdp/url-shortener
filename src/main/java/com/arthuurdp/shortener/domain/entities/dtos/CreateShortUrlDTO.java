package com.arthuurdp.shortener.domain.entities.dtos;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlDTO(
        @NotBlank(message = "Please enter a valid URL")
        @Valid
        @URL
        String originalUrl) {
}
