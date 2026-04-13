package com.pos.tenant.infrastructure.adapter.in.web.dto;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionSummaryResponse(
        UUID tenantId,
        String tenantName,
        String tenantPhone,
        String planName,
        String subscriptionStatus,
        Instant subscriptionEndsAt
) {}
