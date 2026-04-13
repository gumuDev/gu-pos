package com.pos.tenant.domain.port.out;

import java.util.UUID;

public interface AuthTokenPort {
    String generateAccessToken(UUID userId, UUID tenantId);
    String generateRefreshToken(UUID userId);
    String hashToken(String token);
    String hashPassword(String rawPassword);
    boolean verifyPassword(String rawPassword, String encodedPassword);
    UUID extractUserIdFromRefresh(String refreshToken);
}
