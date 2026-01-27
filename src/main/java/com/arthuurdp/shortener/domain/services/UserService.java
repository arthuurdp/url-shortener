package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.entities.user.UpdateUserDTO;
import com.arthuurdp.shortener.domain.entities.user.UserResponseDTO;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;
    private final EntityMapperService entityMapper;

    public UserService(UserRepository repo, EntityMapperService entityMapper) {
        this.repo = repo;
        this.entityMapper = entityMapper;
    }

    public UserResponseDTO findById(Long id) {
        User dto = repo.findByIdWithShortUrls(id).orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
        return entityMapper.toUserDTO(dto);
    }

    public List<UserResponseDTO> findAll() {
        return repo.findAllWithShortUrls().stream().map(entityMapper::toUserDTO).toList();
    }

    public UserResponseDTO updateUser(Long id, UpdateUserDTO dto) {
        User user = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user was found with id " + id));

        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        if (dto.email() != null) {
            user.setEmail(dto.email());
        }
        if (dto.password() != null) {
            user.setPassword(dto.password());
        }
        if (dto.role() != null) {
            user.setRole(dto.role());
        }
        user = repo.save(user);
        return entityMapper.toUserDTO(user);
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }
}
