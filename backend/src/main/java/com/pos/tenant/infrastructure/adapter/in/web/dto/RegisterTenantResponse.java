package com.pos.tenant.infrastructure.adapter.in.web.dto;

import java.util.UUID;

public record RegisterTenantResponse(
        UUID tenantId,
        String businessName
) {}
