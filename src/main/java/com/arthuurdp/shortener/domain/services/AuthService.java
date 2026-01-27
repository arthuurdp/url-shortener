package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.user.CreateUserDTO;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.entities.user.UserDTO;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository repo;
    private final EntityMapperService entityMapper;

    public AuthService(UserRepository repo, EntityMapperService entityMapper) {
        this.repo = repo;
        this.entityMapper = entityMapper;
    }

    public UserDTO register(CreateUserDTO dto) {
        if (this.repo.findByEmail(dto.email()) != null) {
            throw new ResourceNotFoundException("Email " + dto.email() + " is already in use!");
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
            return entityMapper.toUserDTO(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByEmail(username);
    }
}
