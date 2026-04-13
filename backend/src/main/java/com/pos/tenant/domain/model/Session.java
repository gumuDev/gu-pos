package com.pos.tenant.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Session(
        UUID id,
        UUID userId,
        UUID deviceId,
        String refreshTokenHash,
        boolean isRevoked,
        Instant expiresAt
) {
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
