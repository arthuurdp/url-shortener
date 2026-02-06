package com.arthuurdp.shortener.domain.controllers;

import com.arthuurdp.shortener.domain.entities.user.UpdateUserDTO;
import com.arthuurdp.shortener.domain.entities.user.UserWithUrlsDTO;
import com.arthuurdp.shortener.domain.entities.user.UserWithoutUrlsDTO;
import com.arthuurdp.shortener.domain.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Users", description = "User management operations")
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all users without urls",
            description = "Returns a list of all registered users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "include",
            description = "Optional parameter. Use 'urls' to include user urls.",
            example = "/include=urls"
    )
    @GetMapping(params = "!include")
    public ResponseEntity<List<UserWithoutUrlsDTO>> findAllWithoutUrls() {
        return ResponseEntity.ok().body(service.findAllWithoutUrls());
    }
    @GetMapping(params = "include=urls")
    public ResponseEntity<List<UserWithUrlsDTO>> findAllWithUrls() {
        return ResponseEntity.ok().body(service.findAllWithUrls());
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns the user identified by the given ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<UserWithUrlsDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Operation(
            summary = "Update a user by ID",
            description = "Updates user information for the given user ID. The user ID and role cannot be modified."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<UserWithUrlsDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO dto) {
        return ResponseEntity.ok().body(service.updateUser(id, dto));
    }

    @Operation(
            summary = "Delete a user by ID",
            description = "Deletes a user and all associated short URLs."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

