package com.arthuurdp.shortener.domain.controllers;

import com.arthuurdp.shortener.domain.entities.user.AuthDTO;
import com.arthuurdp.shortener.domain.entities.user.CreateUserDTO;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.entities.user.UserDTO;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import com.arthuurdp.shortener.domain.services.EntityMapperService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController{
    private final AuthenticationManager authManager;
    private final EntityMapperService entityMapper;
    private final UserRepository repo;

    public AuthController(AuthenticationManager authManager, EntityMapperService entityMapper, UserRepository repo) {
        this.authManager = authManager;
        this.entityMapper = entityMapper;
        this.repo = repo;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO dto) {
        var authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = this.authManager.authenticate(authToken);


        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid CreateUserDTO dto) {
        if (this.repo.findByEmail(dto.email()) != null) {
            return ResponseEntity.badRequest().build();
        } else {
            String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
            User user = new User(
                    dto.firstName(),
                    dto.lastName(),
                    dto.email(),
                    encryptedPassword,
                    dto.role()
            );
            repo.save(user);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.email())
                .toUri();
        return ResponseEntity.created(uri).body(entityMapper.toUserDTO(user));
    }
    }

}
