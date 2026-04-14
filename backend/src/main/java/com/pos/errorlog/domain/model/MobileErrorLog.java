package com.pos.errorlog.domain.model;

import java.time.Instant;
import java.util.UUID;

public class MobileErrorLog {

    private static final int MAX_MESSAGE_LENGTH = 2000;
    private static final int MAX_STACK_LENGTH = 5000;

    private final UUID id;
    private final UUID tenantId;
    private final UUID userId;
    private final UUID sessionId;
    private final ErrorLevel level;
    private final String message;
    private final String stack;
    private final String screen;
    private final String action;
    private final String platform;
    private final String osVersion;
    private final String deviceModel;
    private final String appVersion;
    private final String buildNumber;
    private final Instant occurredAt;
    private final Instant createdAt;

    public MobileErrorLog(UUID tenantId, UUID userId, UUID sessionId, ErrorLevel level,
                          String message, String stack, String screen, String action,
                          String platform, String osVersion, String deviceModel,
                          String appVersion, String buildNumber, Instant occurredAt) {
        this.id = UUID.randomUUID();
        this.tenantId = tenantId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.level = level;
        this.message = truncate(message, MAX_MESSAGE_LENGTH);
        this.stack = truncate(stack, MAX_STACK_LENGTH);
        this.screen = screen;
        this.action = action;
        this.platform = platform;
        this.osVersion = osVersion;
        this.deviceModel = deviceModel;
        this.appVersion = appVersion;
        this.buildNumber = buildNumber;
        this.occurredAt = occurredAt;
        this.createdAt = Instant.now();
    }

    private String truncate(String value, int max) {
        if (value == null) return null;
        return value.length() <= max ? value : value.substring(0, max);
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public UUID getUserId() { return userId; }
    public UUID getSessionId() { return sessionId; }
    public ErrorLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public String getStack() { return stack; }
    public String getScreen() { return screen; }
    public String getAction() { return action; }
    public String getPlatform() { return platform; }
    public String getOsVersion() { return osVersion; }
    public String getDeviceModel() { return deviceModel; }
    public String getAppVersion() { return appVersion; }
    public String getBuildNumber() { return buildNumber; }
    public Instant getOccurredAt() { return occurredAt; }
    public Instant getCreatedAt() { return createdAt; }
}
