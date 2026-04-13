package com.pos.tenant.domain.model;

import java.util.UUID;

public class User {

    private final UUID id;
    private final UUID tenantId;
    private final String phone;
    private final String passwordHash;
    private final String role;
    private final String name;

    public User(UUID id, UUID tenantId, String phone, String passwordHash, String role, String name) {
        this.id = id;
        this.tenantId = tenantId;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
        this.name = name;
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public String getName() { return name; }
}
