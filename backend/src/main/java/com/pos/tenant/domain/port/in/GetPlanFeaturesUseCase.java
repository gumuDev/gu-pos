package com.pos.tenant.domain.port.in;

import java.util.UUID;

public interface GetPlanFeaturesUseCase {
    PlanFeaturesResult getByTenantId(UUID tenantId);
}
