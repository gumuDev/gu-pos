package com.pos.tenant.domain.service;

import com.pos.tenant.domain.exception.TenantNotFoundException;
import com.pos.tenant.domain.model.Branch;
import com.pos.tenant.domain.model.Plan;
import com.pos.tenant.domain.model.Subscription;
import com.pos.tenant.domain.model.Tenant;
import com.pos.tenant.domain.port.in.GetTenantQuery;
import com.pos.tenant.domain.port.in.GetTenantUseCase;
import com.pos.tenant.domain.port.in.TenantDetail;
import com.pos.tenant.domain.port.out.BranchRepository;
import com.pos.tenant.domain.port.out.PlanRepository;
import com.pos.tenant.domain.port.out.SubscriptionRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetTenantService implements GetTenantUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetTenantService.class);

    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BranchRepository branchRepository;

    public GetTenantService(TenantRepository tenantRepository,
                            PlanRepository planRepository,
                            SubscriptionRepository subscriptionRepository,
                            BranchRepository branchRepository) {
        this.tenantRepository = tenantRepository;
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public TenantDetail get(GetTenantQuery query) {
        log.debug("Getting tenant detail — id={}", query.id());

        Tenant tenant = tenantRepository.findById(query.id())
                .orElseThrow(() -> new TenantNotFoundException(query.id()));

        Plan plan = planRepository.findById(tenant.getPlanId()).orElse(null);
        Subscription subscription = subscriptionRepository.findByTenantId(tenant.getId()).orElse(null);
        Branch branch = branchRepository.findByTenantId(tenant.getId()).orElse(null);
        var history = subscriptionRepository.findHistoryByTenantId(tenant.getId());

        return new TenantDetail(
                tenant.getId(),
                tenant.getName(),
                tenant.getPhone(),
                tenant.getCurrency(),
                tenant.getBusinessType(),
                tenant.getStatus(),
                tenant.getCreatedAt(),
                tenant.getFeaturesConfig(),
                tenant.getPlanId(),
                plan != null ? plan.getName() : "unknown",
                subscription != null ? subscription.getStatus().toValue() : null,
                subscription != null ? subscription.getStartedAt() : null,
                subscription != null ? subscription.getEndsAt() : null,
                history,
                branch != null ? branch.getId() : null,
                branch != null ? branch.getName() : null
        );
    }
}
