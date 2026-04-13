package com.pos.tenant.infrastructure.adapter.in.web.dto;

import java.time.Instant;
import java.util.UUID;

public record TenantSummaryResponse(UUID id, String name, String phone, String planName, Instant createdAt) {}
