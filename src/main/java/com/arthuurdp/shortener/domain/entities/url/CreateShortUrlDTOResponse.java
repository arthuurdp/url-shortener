package com.arthuurdp.shortener.domain.entities.url;

// when an original url is sent, it returns the short key
public record CreateShortUrlDTOResponse(String shortKey) {
}
