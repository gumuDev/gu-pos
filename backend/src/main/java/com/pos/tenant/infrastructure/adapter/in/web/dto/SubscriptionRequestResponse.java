package com.pos.tenant.infrastructure.adapter.in.web.dto;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionRequestResponse(
        UUID id,
        UUID tenantId,
        String planName,
        String transactionRef,
        String receiptUrl,
        String status,
        Instant createdAt
) {}
