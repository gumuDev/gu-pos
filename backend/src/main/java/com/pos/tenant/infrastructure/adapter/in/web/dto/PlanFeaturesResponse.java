package com.pos.tenant.infrastructure.adapter.in.web.dto;

import java.time.Instant;
import java.util.UUID;

public record PlanFeaturesResponse(
        String planName,
        boolean active,
        Instant endsAt,
        boolean sync,
        boolean advancedReports,
        int maxCashiers,
        Integer maxBranches,
        boolean multiBranch,
        boolean telegramStockAlerts,
        boolean csvExport,
        UUID branchId
) {}
