package com.ra.base_spring_boot.security.principle;

import com.ra.base_spring_boot.exception.TechnicalException;

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
            for (byte hashByte : hashBytes) {
                hexString.append(String.format("%02x", hashByte));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new TechnicalException("Hash SHA-256 failed", e);
        }
    }
}
