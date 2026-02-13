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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final EntityMapperService entityMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public UserService(UserRepository repo, EntityMapperService entityMapper, PasswordEncoder passwordEncoder, AuthService authService) {
        this.repo = repo;
        this.entityMapper = entityMapper;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserWithUrlsDTO findById(Long id) {
        User dto = repo.findByIdWithShortUrls(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return entityMapper.toUserWithUrlsDTO(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserWithoutUrlsDTO> findAllWithoutUrls(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = repo.findAll(pageable);
        return userPage.map(entityMapper::toUserWithoutUrlsDTO);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserWithoutUrlsDTO updateUser(Long id, UpdateUserDTO dto) {
        User targetUser = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.firstName() != null && !dto.firstName().isBlank()) {
            targetUser.setFirstName(dto.firstName());
        }

        if (dto.lastName() != null && !dto.lastName().isBlank()) {
            targetUser.setLastName(dto.lastName());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            if (!dto.email().equals(targetUser.getEmail())) {
                if (repo.findByEmail(dto.email()) != null) {
                    throw new IllegalArgumentException("Email already in use");
                }
            }
            targetUser.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            targetUser.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (dto.role() != null) {
            User currentUser = authService.getCurrentUser();
            boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
            boolean isUpdatingSelf = currentUser.getId().equals(targetUser.getId());

            if (!isAdmin) {
                throw new AccessDeniedException("Only administrators can change user roles");
            }
            if (isUpdatingSelf) {
                throw new AccessDeniedException("You cannot change your own role. Ask another administrator.");
            }
            targetUser.setRole(dto.role());
        }

        return entityMapper.toUserWithoutUrlsDTO(targetUser);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {

        User currentUser = authService.getCurrentUser();
        User targetUser = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new AccessDeniedException("Administrators cannot delete themselves");
        }

        repo.delete(targetUser);
    }

}
