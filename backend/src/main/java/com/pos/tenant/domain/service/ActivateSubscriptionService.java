package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.Branch;
import com.pos.tenant.domain.model.Plan;
import com.pos.tenant.domain.model.Subscription;
import com.pos.tenant.domain.model.SubscriptionStatus;
import com.pos.tenant.domain.port.in.ActivateSubscriptionCommand;
import com.pos.tenant.domain.port.in.ActivateSubscriptionUseCase;
import com.pos.tenant.domain.port.out.BranchRepository;
import com.pos.tenant.domain.port.out.PlanRepository;
import com.pos.tenant.domain.port.out.SubscriptionRepository;
import com.pos.tenant.domain.port.out.SubscriptionRequestRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ActivateSubscriptionService implements ActivateSubscriptionUseCase {

    private static final Logger log = LoggerFactory.getLogger(ActivateSubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionRequestRepository requestRepository;
    private final PlanRepository planRepository;
    private final TenantRepository tenantRepository;
    private final BranchRepository branchRepository;

    public ActivateSubscriptionService(SubscriptionRepository subscriptionRepository,
                                       SubscriptionRequestRepository requestRepository,
                                       PlanRepository planRepository,
                                       TenantRepository tenantRepository,
                                       BranchRepository branchRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.requestRepository = requestRepository;
        this.planRepository = planRepository;
        this.tenantRepository = tenantRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public void activate(ActivateSubscriptionCommand command) {
        log.info("Activating subscription — tenantId={} plan={}", command.tenantId(), command.planName());

        tenantRepository.findById(command.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("tenant_not_found"));

        Plan plan = planRepository.findByName(command.planName())
                .orElseThrow(() -> new IllegalArgumentException("plan_not_found: " + command.planName()));

        Instant now = Instant.now();
        Instant endsAt = now.plus(30, ChronoUnit.DAYS);

        subscriptionRepository.save(new Subscription(
                UUID.randomUUID(),
                command.tenantId(),
                plan.getId(),
                SubscriptionStatus.ACTIVE,
                now,
                endsAt
        ));
        subscriptionRepository.saveHistory(new com.pos.tenant.domain.model.SubscriptionHistory(
                UUID.randomUUID(),
                command.tenantId(),
                plan.getId(),
                "active",
                now,
                endsAt,
                now
        ));

        // Create principal branch if not yet exists
        branchRepository.findByTenantId(command.tenantId()).orElseGet(() -> {
            Branch branch = branchRepository.save(new Branch(UUID.randomUUID(), command.tenantId(), "Principal"));
            log.info("Branch created — tenantId={} branchId={}", command.tenantId(), branch.getId());
            return branch;
        });

        requestRepository.findPendingByTenantId(command.tenantId()).ifPresent(req -> {
            req.approve();
            requestRepository.save(req);
        });

        log.info("Subscription activated — tenantId={} plan={} endsAt={}", command.tenantId(), plan.getName(), endsAt);
    }
}
