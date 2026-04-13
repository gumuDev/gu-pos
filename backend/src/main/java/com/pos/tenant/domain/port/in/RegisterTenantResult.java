package com.pos.tenant.domain.port.in;

import java.util.UUID;

public record RegisterTenantResult(
        UUID tenantId,
        String businessName
) {}
