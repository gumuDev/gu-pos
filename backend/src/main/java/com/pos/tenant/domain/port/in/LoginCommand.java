package com.pos.tenant.domain.port.in;

import java.util.UUID;

public record LoginCommand(
        String phone,
        String password,
        String deviceFingerprint
) {}
