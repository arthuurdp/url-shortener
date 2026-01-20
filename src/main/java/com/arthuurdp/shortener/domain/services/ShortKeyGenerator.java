package com.arthuurdp.shortener.domain.services;

import java.security.SecureRandom;

public class ShortKeyGenerator {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int KEY_LENGTH = 6;

    public static String generate() {
        StringBuilder key = new StringBuilder(KEY_LENGTH);
        for (int i = 0; i < KEY_LENGTH; i++) {
            int index = RANDOM.nextInt(BASE62.length());
            key.append(BASE62.charAt(index));
        }
        return key.toString();
    }
}
