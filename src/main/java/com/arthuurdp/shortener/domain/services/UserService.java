package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.entities.user.UpdateUserDTO;
import com.arthuurdp.shortener.domain.entities.user.UserResponseDTO;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import com.arthuurdp.shortener.domain.services.exceptions.AccessDeniedException;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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

    public UserResponseDTO findById(Long id) {
        User dto = repo.findByIdWithShortUrls(id).orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
        return entityMapper.toUserDTO(dto);
    }

    public List<UserResponseDTO> findAll() {
        return repo.findAllWithShortUrls().stream().map(entityMapper::toUserDTO).toList();
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserDTO dto) {
        User targetUser = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user was found with id " + id));
        User currentUser = authService.getCurrentUser();

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isSelf  = currentUser.getId().equals(targetUser.getId());

        if (!isAdmin && !isSelf) {
            throw new AccessDeniedException("You are not allowed to update this user");
        }

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
            if (!isAdmin) {
                throw new AccessDeniedException("Only admin can change user roles");
            }
            targetUser.setRole(dto.role());
        }

        User saved = repo.save(targetUser);
        return entityMapper.toUserDTO(saved);
    }


    public void deleteUser(Long id) {
        repo.deleteById(id);
    }
}
