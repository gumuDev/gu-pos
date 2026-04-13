package com.pos.tenant.domain.model;

import java.util.UUID;

public class Branch {

    private final UUID id;
    private final UUID tenantId;
    private final String name;

    public Branch(UUID id, UUID tenantId, String name) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public String getName() { return name; }
}
