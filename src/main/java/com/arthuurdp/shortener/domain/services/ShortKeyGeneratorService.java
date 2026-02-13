package com.arthuurdp.shortener.domain.services;

import org.springframework.stereotype.Service;

@Service
public class ShortKeyGeneratorService {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String encode(long value) {
        if (value == 0) return "0";

        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % 62)));
            value /= 62;
        }

        return sb.reverse().toString();
    }
}
