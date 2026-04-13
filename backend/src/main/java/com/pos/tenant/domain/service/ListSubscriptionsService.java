package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.Subscription;
import com.pos.tenant.domain.model.Tenant;
import com.pos.tenant.domain.port.in.ListSubscriptionsUseCase;
import com.pos.tenant.domain.port.in.ListTenantsQuery;
import com.pos.tenant.domain.port.in.SubscriptionSummary;
import com.pos.tenant.domain.port.out.PlanRepository;
import com.pos.tenant.domain.port.out.SubscriptionRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListSubscriptionsService implements ListSubscriptionsUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListSubscriptionsService.class);

    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ListSubscriptionsService(TenantRepository tenantRepository,
                                    PlanRepository planRepository,
                                    SubscriptionRepository subscriptionRepository) {
        this.tenantRepository = tenantRepository;
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public PageResult<SubscriptionSummary> list(ListTenantsQuery query) {
        log.debug("Listing subscriptions — page={} size={}", query.page(), query.size());
        PageResult<Tenant> result = tenantRepository.findAll(query.page(), query.size());

        List<SubscriptionSummary> summaries = result.data().stream()
                .flatMap(tenant -> {
                    Subscription sub = subscriptionRepository.findByTenantId(tenant.getId()).orElse(null);
                    if (sub == null) return java.util.stream.Stream.empty();
                    String planName = planRepository.findById(tenant.getPlanId())
                            .map(p -> p.getName())
                            .orElse("unknown");
                    return java.util.stream.Stream.of(new SubscriptionSummary(
                            tenant.getId(),
                            tenant.getName(),
                            tenant.getPhone(),
                            planName,
                            sub.getStatus().toValue(),
                            sub.getEndsAt()
                    ));
                })
                .toList();

        return new PageResult<>(summaries, result.total(), result.page(), result.size());
    }
}
