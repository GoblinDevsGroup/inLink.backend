package org.example.adds.Users;

import java.util.Random;

public class CustomUUIDGenerator {

    private static final Random RANDOM = new Random();

    public static String generateCustomUUID() {
        StringBuilder uuid = new StringBuilder("U");
        for (int i = 0; i < 5; i++) {
            int digit = RANDOM.nextInt(10);
            uuid.append(digit);
        }
        return uuid.toString();
    }
}
