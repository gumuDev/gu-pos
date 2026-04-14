package com.pos.errorlog.domain.service;

import com.pos.errorlog.domain.model.ErrorLevel;
import com.pos.errorlog.domain.model.MobileErrorLog;
import com.pos.errorlog.domain.port.in.CreateErrorLogUseCase;
import com.pos.errorlog.domain.port.out.ErrorLogRepository;

import java.time.Instant;
import java.util.UUID;

public class CreateErrorLogService implements CreateErrorLogUseCase {

    private final ErrorLogRepository repository;

    public CreateErrorLogService(ErrorLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public UUID create(UUID tenantId, UUID userId, UUID sessionId, ErrorLevel level,
                       String message, String stack, String screen, String action,
                       String platform, String osVersion, String deviceModel,
                       String appVersion, String buildNumber, Instant occurredAt) {
        MobileErrorLog log = new MobileErrorLog(
            tenantId, userId, sessionId, level,
            message, stack, screen, action,
            platform, osVersion, deviceModel,
            appVersion, buildNumber, occurredAt
        );
        repository.save(log);
        return log.getId();
    }
}
