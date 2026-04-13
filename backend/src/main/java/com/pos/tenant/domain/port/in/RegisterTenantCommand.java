package com.pos.tenant.domain.port.in;

import com.pos.tenant.domain.model.FeatureFlags;

public record RegisterTenantCommand(
        String businessName,
        String phone,
        String currency,
        String businessType,
        FeatureFlags features
) {}
