package com.pos.tenant.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Tenant {

    private final UUID id;
    private final String name;
    private final String phone;
    private final String currency;
    private final String businessType;
    private final FeatureFlags featuresConfig;
    private final UUID planId;
    private final String status;
    private final Instant createdAt;
    private final Long telegramChatId;

    public Tenant(UUID id, String name, String phone, String currency,
                  String businessType, FeatureFlags featuresConfig,
                  UUID planId, String status, Instant createdAt) {
        this(id, name, phone, currency, businessType, featuresConfig, planId, status, createdAt, null);
    }

    public Tenant(UUID id, String name, String phone, String currency,
                  String businessType, FeatureFlags featuresConfig,
                  UUID planId, String status, Instant createdAt, Long telegramChatId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.currency = currency;
        this.businessType = businessType;
        this.featuresConfig = featuresConfig;
        this.planId = planId;
        this.status = status;
        this.createdAt = createdAt;
        this.telegramChatId = telegramChatId;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getCurrency() { return currency; }
    public String getBusinessType() { return businessType; }
    public FeatureFlags getFeaturesConfig() { return featuresConfig; }
    public UUID getPlanId() { return planId; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Long getTelegramChatId() { return telegramChatId; }
}
