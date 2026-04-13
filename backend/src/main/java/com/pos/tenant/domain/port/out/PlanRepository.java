package com.pos.tenant.domain.port.out;

import com.pos.tenant.domain.model.Plan;

import java.util.Optional;
import java.util.UUID;

public interface PlanRepository {
    Optional<Plan> findByName(String name);
    Optional<Plan> findById(UUID id);
}
