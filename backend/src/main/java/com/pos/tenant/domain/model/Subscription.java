package com.pos.tenant.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Subscription {

    private final UUID id;
    private final UUID tenantId;
    private final UUID planId;
    private final SubscriptionStatus status;
    private final Instant startedAt;
    private final Instant endsAt;

    public Subscription(UUID id, UUID tenantId, UUID planId, SubscriptionStatus status, Instant startedAt, Instant endsAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.planId = planId;
        this.status = status;
        this.startedAt = startedAt;
        this.endsAt = endsAt;
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public UUID getPlanId() { return planId; }
    public SubscriptionStatus getStatus() { return status; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getEndsAt() { return endsAt; }

    public boolean isActive() {
        return SubscriptionStatus.ACTIVE.equals(status);
    }
}
