package com.arthuurdp.shortener.domain.entities.url;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned when a new short URL is created, containing the generated short key.")
public record CreateShortUrlDTOResponse(String shortKey) {
}
