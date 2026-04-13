package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.PlanFeatures;
import com.pos.tenant.domain.port.in.GetPlanFeaturesUseCase;
import com.pos.tenant.domain.port.in.PlanFeaturesResult;
import com.pos.tenant.domain.port.out.BranchRepository;
import com.pos.tenant.domain.port.out.PlanRepository;
import com.pos.tenant.domain.port.out.SubscriptionRepository;

import java.util.UUID;

public class GetPlanFeaturesService implements GetPlanFeaturesUseCase {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final BranchRepository branchRepository;

    public GetPlanFeaturesService(SubscriptionRepository subscriptionRepository,
                                  PlanRepository planRepository,
                                  BranchRepository branchRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public PlanFeaturesResult getByTenantId(UUID tenantId) {
        UUID branchId = branchRepository.findByTenantId(tenantId)
                .map(b -> b.getId())
                .orElse(null);

        // Check active subscription first
        var active = subscriptionRepository.findActiveByTenantId(tenantId)
                .flatMap(sub -> planRepository.findById(sub.getPlanId())
                        .map(plan -> new PlanFeaturesResult(
                                plan.getName(),
                                plan.getFeatures(),
                                sub.getEndsAt(),
                                true,
                                branchId
                        )));
        if (active.isPresent()) return active.get();

        // No active — check if there was an expired one
        return subscriptionRepository.findByTenantId(tenantId)
                .flatMap(sub -> planRepository.findById(sub.getPlanId())
                        .map(plan -> new PlanFeaturesResult(
                                plan.getName(),
                                plan.getFeatures(),
                                sub.getEndsAt(),
                                false,
                                branchId
                        )))
                .orElseGet(this::freePlan);
    }

    private PlanFeaturesResult freePlan() {
        return planRepository.findByName("local")
                .map(plan -> new PlanFeaturesResult(
                        plan.getName(),
                        PlanFeatures.free(),
                        null,
                        false,
                        null
                ))
                .orElse(new PlanFeaturesResult("local", PlanFeatures.free(), null, false, null));
    }
}
