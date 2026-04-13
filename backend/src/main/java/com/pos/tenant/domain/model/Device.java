package com.pos.tenant.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Device(
        UUID id,
        UUID userId,
        String deviceFingerprint,
        Instant lastLogin
) {}
