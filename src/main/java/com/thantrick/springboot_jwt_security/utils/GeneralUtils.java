package com.thantrick.springboot_jwt_security.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class GeneralUtils {
    private final Random random = new SecureRandom();
    private final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length) {
        return generateRandomId(length);
    }

    public String generateAddressId(int length) {
        return generateRandomId(length);
    }

    private String generateRandomId(int length) {
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new String(randomString);
    }
}
