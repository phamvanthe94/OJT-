package com.ra.base_spring_boot.security.principle;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtil {
    private HashUtil() {
    }

    public static String sha256(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) hexString.append(String.format("%02x", b));
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hash SHA-256 failed");
        }

    }
}
