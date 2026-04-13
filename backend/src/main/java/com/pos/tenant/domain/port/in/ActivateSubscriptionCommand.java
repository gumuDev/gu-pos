package com.pos.tenant.domain.port.in;

import java.util.UUID;

public record ActivateSubscriptionCommand(
        UUID tenantId,
        String planName
) {}
