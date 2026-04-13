package com.pos.tenant.domain.exception;

import java.util.UUID;

public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(UUID id) {
        super("tenant_not_found: " + id);
    }
}
