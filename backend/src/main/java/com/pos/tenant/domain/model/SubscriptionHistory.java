package com.pos.tenant.domain.model;

import java.time.Instant;
import java.util.UUID;

public class SubscriptionHistory {

    private final UUID id;
    private final UUID tenantId;
    private final UUID planId;
    private final String status;
    private final Instant startedAt;
    private final Instant endsAt;
    private final Instant activatedAt;

    public SubscriptionHistory(UUID id, UUID tenantId, UUID planId, String status,
                               Instant startedAt, Instant endsAt, Instant activatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.planId = planId;
        this.status = status;
        this.startedAt = startedAt;
        this.endsAt = endsAt;
        this.activatedAt = activatedAt;
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public UUID getPlanId() { return planId; }
    public String getStatus() { return status; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getEndsAt() { return endsAt; }
    public Instant getActivatedAt() { return activatedAt; }
}
