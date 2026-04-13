package com.pos.support.infrastructure.adapter.in.web.dto;

import com.pos.support.domain.model.SupportReport;
import java.time.Instant;
import java.util.UUID;

public record SupportReportResponse(
        UUID id,
        UUID tenantId,
        String type,
        String description,
        String screenshotUrl,
        String status,
        Instant createdAt
) {
    public static SupportReportResponse from(SupportReport r) {
        return new SupportReportResponse(
                r.getId(), r.getTenantId(), r.getType(),
                r.getDescription(), r.getScreenshotUrl(),
                r.getStatus(), r.getCreatedAt()
        );
    }
}
