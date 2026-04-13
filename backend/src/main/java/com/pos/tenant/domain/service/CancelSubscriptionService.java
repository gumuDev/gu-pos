package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.Subscription;
import com.pos.tenant.domain.model.SubscriptionStatus;
import com.pos.tenant.domain.port.in.CancelSubscriptionUseCase;
import com.pos.tenant.domain.port.out.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class CancelSubscriptionService implements CancelSubscriptionUseCase {

    private static final Logger log = LoggerFactory.getLogger(CancelSubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;

    public CancelSubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void cancel(UUID tenantId) {
        log.info("Canceling subscription — tenantId={}", tenantId);

        Subscription sub = subscriptionRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("subscription_not_found"));

        if (SubscriptionStatus.CANCELED.equals(sub.getStatus())) {
            throw new IllegalStateException("subscription_already_canceled");
        }

        Subscription canceled = new Subscription(
                sub.getId(),
                sub.getTenantId(),
                sub.getPlanId(),
                SubscriptionStatus.CANCELED,
                sub.getStartedAt(),
                sub.getEndsAt()
        );
        subscriptionRepository.save(canceled);

        log.info("Subscription canceled — tenantId={}", tenantId);
    }
}
