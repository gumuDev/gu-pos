package com.pos.tenant.domain.model;

import java.util.UUID;

public class Plan {

    private final UUID id;
    private final String name;
    private final PlanFeatures features;

    public Plan(UUID id, String name, PlanFeatures features) {
        this.id = id;
        this.name = name;
        this.features = features;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public PlanFeatures getFeatures() { return features; }
}
