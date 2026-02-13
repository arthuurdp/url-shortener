package com.arthuurdp.shortener.infrastructure.config;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.url.ShortUrl;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import com.arthuurdp.shortener.domain.services.ShortKeyGeneratorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    private final UserRepository userRepository;
    private final ShortUrlRepository shortUrlRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShortKeyGeneratorService keyGenerator;

    public DataInitializer(UserRepository userRepository, ShortUrlRepository shortUrlRepository, PasswordEncoder passwordEncoder, ShortKeyGeneratorService keyGenerator) {
        this.userRepository = userRepository;
        this.shortUrlRepository = shortUrlRepository;
        this.passwordEncoder = passwordEncoder;
        this.keyGenerator = keyGenerator;
    }

    @Bean
    CommandLineRunner initUsers() {
        return args -> {

            if (userRepository.count() > 0) {
                return;
            }

            User admin1 = new User(
                    "Admin",
                    "1",
                    "admin1@email.com",
                    passwordEncoder.encode("arthur0702"),
                    Role.ROLE_ADMIN
            );

            User user1 = new User(
                    "User",
                    "1",
                    "user1@email.com",
                    passwordEncoder.encode("arthur0702"),
                    Role.ROLE_USER
            );

            User admin2 = new User(
                    "Admin",
                    "2",
                    "admin2@email.com",
                    passwordEncoder.encode("arthur0702"),
                    Role.ROLE_ADMIN
            );

            User user2 = new User(
                    "User",
                    "2",
                    "user2@email.com",
                    passwordEncoder.encode("arthur0702"),
                    Role.ROLE_USER
            );

            userRepository.save(admin1);
            userRepository.save(user1);
            userRepository.save(admin2);
            userRepository.save(user2);

            ShortUrl shortUrl1 = new ShortUrl("https://youtube.com", admin1);
            shortUrlRepository.saveAndFlush(shortUrl1);
            shortUrl1.setShortKey(keyGenerator.encode(shortUrl1.getId()));

            ShortUrl shortUrl2 = new ShortUrl("https://github.com", admin2);
            shortUrlRepository.saveAndFlush(shortUrl2);
            shortUrl2.setShortKey(keyGenerator.encode(shortUrl2.getId()));

            ShortUrl shortUrl3 = new ShortUrl("https://reddit.com", user1);
            shortUrlRepository.saveAndFlush(shortUrl3);
            shortUrl3.setShortKey(keyGenerator.encode(shortUrl3.getId()));

            ShortUrl shortUrl4 = new ShortUrl("https://github.com/arthuurdp/url-shortener", user2);
            shortUrlRepository.saveAndFlush(shortUrl4);
            shortUrl4.setShortKey(keyGenerator.encode(shortUrl4.getId()));

            shortUrlRepository.save(shortUrl1);
            shortUrlRepository.save(shortUrl2);
            shortUrlRepository.save(shortUrl3);
            shortUrlRepository.save(shortUrl4);
        };
    }
}

