package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.entities.user.UpdateUserDTO;
import com.arthuurdp.shortener.domain.entities.user.UserWithUrlsDTO;
import com.arthuurdp.shortener.domain.entities.user.UserWithoutUrlsDTO;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import com.arthuurdp.shortener.domain.services.exceptions.AccessDeniedException;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;
    private final EntityMapperService entityMapper;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, EntityMapperService entityMapper, AuthService authService, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.entityMapper = entityMapper;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserWithUrlsDTO findById(Long id) {
        User dto = repo.findByIdWithShortUrls(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return entityMapper.toUserWithUrlsDTO(dto);
    }

    public List<UserWithoutUrlsDTO> findAllWithoutUrls() {
        return repo.findAll().stream().map(entityMapper::toUserWithoutUrlsDTO).toList();
    }

    public List<UserWithUrlsDTO> findAllWithUrls() {
        return repo.findAllWithShortUrls().stream().map(entityMapper::toUserWithUrlsDTO).toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserWithUrlsDTO updateUser(Long id, UpdateUserDTO dto) {
        User targetUser = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.firstName() != null) {
            targetUser.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            targetUser.setLastName(dto.lastName());
        }
        if (dto.email() != null) {
            targetUser.setEmail(dto.email());
        }
        if (dto.password() != null) {
            targetUser.setPassword(passwordEncoder.encode(dto.password()));
        }
        if (dto.role() != null) {
            targetUser.setRole(dto.role());
        }

        return entityMapper.toUserWithUrlsDTO(repo.save(targetUser));
    }

    public void deleteUser(Long id) {
        User user = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        repo.delete(user);
    }
}
