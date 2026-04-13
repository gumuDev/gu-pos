package com.pos.tenant.infrastructure.adapter.out.persistence.memory;

import com.pos.tenant.domain.model.Plan;
import com.pos.tenant.domain.model.PlanFeatures;
import com.pos.tenant.domain.port.out.PlanRepository;

import java.util.Optional;
import java.util.UUID;

public class InMemoryPlanRepository implements PlanRepository {

    private static final Plan FREE_PLAN = new Plan(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "local",
            PlanFeatures.free()
    );

    @Override
    public Optional<Plan> findByName(String name) {
        if ("local".equals(name)) {
            return Optional.of(FREE_PLAN);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Plan> findById(UUID id) {
        if (FREE_PLAN.getId().equals(id)) {
            return Optional.of(FREE_PLAN);
        }
        return Optional.empty();
    }
}
