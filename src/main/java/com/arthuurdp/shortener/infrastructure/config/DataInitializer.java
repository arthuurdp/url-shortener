package com.arthuurdp.shortener.infrastructure.config;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initUsers() {
        return args -> {

            if (userRepository.count() > 0) {
                return;
            }

            User admin = new User(
                    "Admin",
                    "User",
                    "admin@email.com",
                    passwordEncoder.encode("arthur0702"),
                    Role.ROLE_ADMIN
            );

            User user = new User(
                    "Default",
                    "User",
                    "user@email.com",
                    passwordEncoder.encode("arthur0702"),
                    Role.ROLE_USER
            );

            userRepository.save(admin);
            userRepository.save(user);
        };
    }
}

