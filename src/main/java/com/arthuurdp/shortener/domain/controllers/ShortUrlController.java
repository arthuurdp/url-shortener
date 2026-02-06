package com.arthuurdp.shortener.domain.controllers;

import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTOResponse;
import com.arthuurdp.shortener.domain.entities.url.ShortUrlDTO;
import com.arthuurdp.shortener.domain.services.ShortUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Short URLs", description = "URL shortening operations")
@RequestMapping("/short-urls")
public class ShortUrlController {

    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all short URLs",
            description = "Retrieves a list of all short URLs created."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of short URLs retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<ShortUrlDTO>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @Operation(
            summary = "Redirect to original URL",
            description = "Redirects to the original URL for a given short key."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirected to original URL"),
            @ApiResponse(responseCode = "404", description = "Short URL not found")
    })
    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortKey) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(service.getOriginalUrl(shortKey)))
                .build();
    }

    @Operation(
            summary = "Create a new short URL",
            description = "Creates a new short URL for the given original URL."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Short URL created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<CreateShortUrlDTOResponse> createShortUrl(@Valid @RequestBody CreateShortUrlDTO dto) {
        CreateShortUrlDTOResponse created = service.createShortUrl(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{shortKey}")
                .buildAndExpand(created.shortKey())
                .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @Operation(
            summary = "Delete a short URL by ID",
            description = "Deletes the short URL identified by the given ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Short URL deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Short URL not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @Valid Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

