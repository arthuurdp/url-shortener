package com.arthuurdp.shortener.domain.controllers;

import com.arthuurdp.shortener.domain.entities.dtos.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.dtos.ShortUrlDTO;
import com.arthuurdp.shortener.domain.services.ShortUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/")
public class ShortUrlController {
    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @GetMapping(value = "/{shortKey}")
    public ResponseEntity<Map<String, String>> getOriginalUrl(@PathVariable String shortKey) {
        String originalUrl = service.getOriginalUrl(shortKey);
        Map<String, String> response = new HashMap<>();
        response.put("originalUrl", originalUrl);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/r/{shortKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortKey) {
        String originalUrl = service.getOriginalUrl(shortKey);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl)).build();
    }

    @PostMapping
    public ResponseEntity<ShortUrlDTO> insert(@RequestBody CreateShortUrlDTO dto) {
        ShortUrlDTO created = service.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{shortKey}")
                .buildAndExpand(created.shortKey())
                .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping(value = "/{shortKey}")
    public ResponseEntity<Void> deleteByShortKey(@PathVariable String shortKey) {
        service.deleteByShortKey(shortKey);
        return ResponseEntity.noContent().build();
    }
}
