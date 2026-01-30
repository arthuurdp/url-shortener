package com.arthuurdp.shortener.domain.controllers;

import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTOResponse;
import com.arthuurdp.shortener.domain.entities.url.ShortUrlDTO;
import com.arthuurdp.shortener.domain.services.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/short-urls")
public class ShortUrlController {
    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ShortUrlDTO>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @GetMapping(value = "/{shortKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortKey) {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(service.getOriginalUrl(shortKey))).build();
    }

    @PostMapping
    public ResponseEntity<CreateShortUrlDTOResponse> createShortUrl(@RequestBody @Valid CreateShortUrlDTO dto) {
        CreateShortUrlDTOResponse created = service.createShortUrl(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{shortKey}")
                .buildAndExpand(created.shortKey())
                .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @Valid Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
