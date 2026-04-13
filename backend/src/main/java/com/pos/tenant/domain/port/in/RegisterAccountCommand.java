package com.pos.tenant.domain.port.in;

import java.util.UUID;

public record RegisterAccountCommand(
        UUID tenantId,
        String phone,
        String password,
        String deviceFingerprint
) {}
