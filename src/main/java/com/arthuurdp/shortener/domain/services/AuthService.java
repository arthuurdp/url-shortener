package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.user.*;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import com.arthuurdp.shortener.infrastructure.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserRepository repo;
    private final EntityMapperService entityMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authManager,
            TokenService tokenService,
            UserRepository repo,
            EntityMapperService entityMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.repo = repo;
        this.entityMapper = entityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(AuthDTO dto) {
        var tokenAuth = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = authManager.authenticate(tokenAuth);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }

    public UserWithoutUrlsDTO register(RegisterUserDTO dto) {
        if (repo.findByEmail(dto.email()) != null) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User(
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                passwordEncoder.encode(dto.password()),
                dto.role()
        );

        repo.save(user);
        return entityMapper.toUserWithoutUrlsDTO(user);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}

