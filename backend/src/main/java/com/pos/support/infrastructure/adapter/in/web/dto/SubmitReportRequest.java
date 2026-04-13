package com.pos.support.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SubmitReportRequest(
        @NotNull UUID tenantId,
        @NotBlank String type,
        @NotBlank String description,
        String screenshotUrl
) {}
