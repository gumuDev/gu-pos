package com.pos.tenant.infrastructure.adapter.in.web.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TenantDetailResponse(
        UUID id,
        String name,
        String phone,
        String currency,
        String businessType,
        String status,
        Instant createdAt,
        FeatureFlagsResponse featuresConfig,
        UUID planId,
        String planName,
        String subscriptionStatus,
        Instant subscriptionStartedAt,
        Instant subscriptionEndsAt,
        List<SubscriptionHistoryItem> subscriptionHistory,
        UUID branchId,
        String branchName
) {
    public record SubscriptionHistoryItem(
            UUID id,
            UUID planId,
            String status,
            Instant startedAt,
            Instant endsAt,
            Instant activatedAt
    ) {}
}
