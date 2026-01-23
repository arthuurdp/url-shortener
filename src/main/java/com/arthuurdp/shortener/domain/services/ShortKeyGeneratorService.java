package com.arthuurdp.shortener.domain.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ShortKeyGeneratorService {

    private static final String BASE62 =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int KEY_LENGTH = 7;

    public String generate() {
        StringBuilder key = new StringBuilder(KEY_LENGTH);
        for (int i = 0; i < KEY_LENGTH; i++) {
            key.append(BASE62.charAt(RANDOM.nextInt(BASE62.length())));
        }
        return key.toString();
    }
}
