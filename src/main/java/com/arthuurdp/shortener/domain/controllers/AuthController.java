package com.arthuurdp.shortener.domain.controllers;

import com.arthuurdp.shortener.domain.entities.user.*;
import com.arthuurdp.shortener.domain.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "Authentication", description = "Login and register endpoints")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(
            summary = "Login a user",
            description = "Authenticates a user with email and password and returns a JWT access token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest dto) {
        return ResponseEntity.ok().body(service.login(dto));
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user and returns the user details. The email must be unique."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data or email already exists"),
    })

    @PostMapping("/register")
    public ResponseEntity<UserWithoutUrlsDTO> register(@RequestBody @Valid RegisterUserRequest dto) {
        UserWithoutUrlsDTO response = service.register(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.email())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
