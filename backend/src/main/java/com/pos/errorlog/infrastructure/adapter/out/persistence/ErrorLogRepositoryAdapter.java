package com.pos.errorlog.infrastructure.adapter.out.persistence;

import com.pos.errorlog.domain.model.MobileErrorLog;
import com.pos.errorlog.domain.port.out.ErrorLogRepository;
import com.pos.errorlog.infrastructure.adapter.out.persistence.entity.MobileErrorLogEntity;
import com.pos.errorlog.infrastructure.adapter.out.persistence.repository.JpaErrorLogRepository;

public class ErrorLogRepositoryAdapter implements ErrorLogRepository {

    private final JpaErrorLogRepository jpa;

    public ErrorLogRepositoryAdapter(JpaErrorLogRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(MobileErrorLog log) {
        jpa.save(toEntity(log));
    }

    private MobileErrorLogEntity toEntity(MobileErrorLog log) {
        MobileErrorLogEntity e = new MobileErrorLogEntity();
        e.setId(log.getId());
        e.setTenantId(log.getTenantId());
        e.setUserId(log.getUserId());
        e.setSessionId(log.getSessionId());
        e.setLevel(log.getLevel().name());
        e.setMessage(log.getMessage());
        e.setStack(log.getStack());
        e.setScreen(log.getScreen());
        e.setAction(log.getAction());
        e.setPlatform(log.getPlatform());
        e.setOsVersion(log.getOsVersion());
        e.setDeviceModel(log.getDeviceModel());
        e.setAppVersion(log.getAppVersion());
        e.setBuildNumber(log.getBuildNumber());
        e.setOccurredAt(log.getOccurredAt());
        e.setCreatedAt(log.getCreatedAt());
        return e;
    }
}
