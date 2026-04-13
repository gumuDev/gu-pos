package com.pos.tenant.domain.port.in;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionSummary(
        UUID tenantId,
        String tenantName,
        String tenantPhone,
        String planName,
        String subscriptionStatus,
        Instant subscriptionEndsAt
) {}
