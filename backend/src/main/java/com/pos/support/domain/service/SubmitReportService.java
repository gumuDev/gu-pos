package com.pos.support.domain.service;

import com.pos.support.domain.model.SupportReport;
import com.pos.support.domain.port.in.SubmitReportUseCase;
import com.pos.support.domain.port.out.SupportNotificationPort;
import com.pos.support.domain.port.out.SupportReportRepository;
import com.pos.tenant.domain.port.out.TenantRepository;

import java.util.UUID;

public class SubmitReportService implements SubmitReportUseCase {

    private final SupportReportRepository repository;
    private final SupportNotificationPort notification;
    private final TenantRepository tenantRepository;

    public SubmitReportService(SupportReportRepository repository,
                               SupportNotificationPort notification,
                               TenantRepository tenantRepository) {
        this.repository = repository;
        this.notification = notification;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public void submit(UUID tenantId, String type, String description, String screenshotUrl) {
        SupportReport report = new SupportReport(tenantId, type, description, screenshotUrl);
        repository.save(report);
        String tenantName = tenantRepository.findById(tenantId)
                .map(t -> t.getName())
                .orElse(tenantId.toString());
        notification.notifyNewReport(report, tenantName);
    }
}
