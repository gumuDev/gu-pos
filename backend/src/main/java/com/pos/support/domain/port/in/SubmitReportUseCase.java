package com.pos.support.domain.port.in;

import java.util.UUID;

public interface SubmitReportUseCase {
    void submit(UUID tenantId, String type, String description, String screenshotUrl);
}
