package com.pos.support.domain.model;

import java.time.Instant;
import java.util.UUID;

public class SupportReport {

    private UUID id;
    private UUID tenantId;
    private String type;
    private String description;
    private String screenshotUrl;
    private String status;
    private Instant createdAt;

    public SupportReport(UUID tenantId, String type, String description, String screenshotUrl) {
        this.id = UUID.randomUUID();
        this.tenantId = tenantId;
        this.type = type;
        this.description = description;
        this.screenshotUrl = screenshotUrl;
        this.status = "pending";
        this.createdAt = Instant.now();
    }

    public SupportReport(UUID id, UUID tenantId, String type, String description,
                         String screenshotUrl, String status, Instant createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.type = type;
        this.description = description;
        this.screenshotUrl = screenshotUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getScreenshotUrl() { return screenshotUrl; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public void markReviewed() { this.status = "reviewed"; }
}
