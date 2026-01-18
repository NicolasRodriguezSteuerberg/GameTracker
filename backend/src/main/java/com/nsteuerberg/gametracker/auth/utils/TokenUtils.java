package com.nsteuerberg.gametracker.auth.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class TokenUtils {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            // Lo convertimos a Base64 para guardarlo como texto bonito en Postgres
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error fatal: No se encuentra SHA-256", e);
        }
    }
}
