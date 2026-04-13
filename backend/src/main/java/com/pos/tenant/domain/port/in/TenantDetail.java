package com.pos.tenant.domain.port.in;

import com.pos.tenant.domain.model.FeatureFlags;
import com.pos.tenant.domain.model.SubscriptionHistory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TenantDetail(
        UUID id,
        String name,
        String phone,
        String currency,
        String businessType,
        String status,
        Instant createdAt,
        FeatureFlags featuresConfig,
        UUID planId,
        String planName,
        String subscriptionStatus,
        Instant subscriptionStartedAt,
        Instant subscriptionEndsAt,
        List<SubscriptionHistory> subscriptionHistory,
        UUID branchId,
        String branchName
) {}
