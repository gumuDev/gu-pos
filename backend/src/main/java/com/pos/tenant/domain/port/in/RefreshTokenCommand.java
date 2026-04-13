package com.pos.tenant.domain.port.in;

public record RefreshTokenCommand(
        String refreshToken,
        String deviceFingerprint
) {}
