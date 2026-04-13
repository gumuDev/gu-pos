package com.pos.tenant.infrastructure.security;

import com.pos.tenant.domain.port.out.AuthTokenPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Component
public class AuthTokenAdapter implements AuthTokenPort {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthTokenAdapter(JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String generateAccessToken(UUID userId, UUID tenantId) {
        return jwtService.generateAccessToken(userId, tenantId);
    }

    @Override
    public String generateRefreshToken(UUID userId) {
        return jwtService.generateRefreshToken(userId);
    }

    @Override
    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    @Override
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public UUID extractUserIdFromRefresh(String refreshToken) {
        return jwtService.extractUserId(refreshToken);
    }
}
