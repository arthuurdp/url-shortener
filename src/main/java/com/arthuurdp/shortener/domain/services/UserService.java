package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.User;
import com.arthuurdp.shortener.domain.entities.dtos.UpdateUserDTO;
import com.arthuurdp.shortener.domain.entities.dtos.UserDTO;
import com.arthuurdp.shortener.domain.entities.enums.Role;
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

    public UserDTO findById(Long id) {
        User dto = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
        return entityMapper.toUserDTO(dto);
    }

    public List<UserDTO> findAll() {
        return repo.findAll().stream().map(entityMapper::toUserDTO).toList();
    }

    public UserDTO createUser(String firstName, String lastName, String email, String password, Role role) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        repo.save(user);
        return entityMapper.toUserDTO(user);
    }

    public UserDTO updateUser(Long id, UpdateUserDTO dto) {
        User user = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user was found with id " + id));

        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(dto.password());

        user = repo.save(user);
        return entityMapper.toUserDTO(user);
    }


    public void deleteUser(Long id) {
        repo.deleteById(id);
    }
}
