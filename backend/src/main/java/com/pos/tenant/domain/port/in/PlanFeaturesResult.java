package com.pos.tenant.domain.port.in;

import com.pos.tenant.domain.model.PlanFeatures;

import java.time.Instant;
import java.util.UUID;

public record PlanFeaturesResult(
        String planName,
        PlanFeatures features,
        Instant endsAt,
        boolean active,
        UUID branchId
) {}
