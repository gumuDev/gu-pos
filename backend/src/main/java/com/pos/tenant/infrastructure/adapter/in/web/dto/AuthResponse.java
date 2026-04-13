package com.pos.tenant.infrastructure.adapter.in.web.dto;

import java.time.Instant;
import java.util.UUID;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        Instant sessionExpiresAt,
        UUID tenantId
) {}
