package com.pos.errorlog.domain.port.in;

import com.pos.errorlog.domain.model.ErrorLevel;

import java.time.Instant;
import java.util.UUID;

public interface CreateErrorLogUseCase {

    UUID create(UUID tenantId, UUID userId, UUID sessionId, ErrorLevel level,
                String message, String stack, String screen, String action,
                String platform, String osVersion, String deviceModel,
                String appVersion, String buildNumber, Instant occurredAt);
}
