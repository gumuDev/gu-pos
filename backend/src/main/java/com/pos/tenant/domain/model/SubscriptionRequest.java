package com.pos.tenant.domain.model;

import java.time.Instant;
import java.util.UUID;

public class SubscriptionRequest {

    private final UUID id;
    private final UUID tenantId;
    private final String planName;
    private final String transactionRef;
    private final String receiptUrl;
    private String status; // pending | approved | rejected
    private final Instant createdAt;

    public SubscriptionRequest(UUID id, UUID tenantId, String planName,
                               String transactionRef, String receiptUrl,
                               String status, Instant createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.planName = planName;
        this.transactionRef = transactionRef;
        this.receiptUrl = receiptUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public String getPlanName() { return planName; }
    public String getTransactionRef() { return transactionRef; }
    public String getReceiptUrl() { return receiptUrl; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public void approve() { this.status = "approved"; }
    public void reject() { this.status = "rejected"; }
}
