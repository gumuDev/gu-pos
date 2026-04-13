package com.pos.tenant.domain.port.in;

import java.time.Instant;
import java.util.UUID;

public record AuthTokenResult(
        String accessToken,
        String refreshToken,
        Instant sessionExpiresAt,
        UUID tenantId
) {}
