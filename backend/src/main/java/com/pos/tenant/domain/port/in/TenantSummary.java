package com.pos.tenant.domain.port.in;

import java.time.Instant;
import java.util.UUID;

public record TenantSummary(UUID id, String name, String phone, String planName, Instant createdAt) {}
