package com.pos.tenant.infrastructure.job;

import com.pos.tenant.domain.model.Subscription;
import com.pos.tenant.domain.model.SubscriptionStatus;
import com.pos.tenant.domain.port.out.PlanRepository;
import com.pos.tenant.domain.port.out.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.UUID;

public class SubscriptionDowngradeJob {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionDowngradeJob.class);

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    public SubscriptionDowngradeJob(SubscriptionRepository subscriptionRepository,
                                    PlanRepository planRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void downgradeExpiredSubscriptions() {
        List<Subscription> expired = subscriptionRepository.findExpiredActive();
        if (expired.isEmpty()) {
            return;
        }

        UUID freePlanId = planRepository.findByName("local")
                .map(p -> p.getId())
                .orElse(null);

        for (Subscription sub : expired) {
            Subscription downgraded = new Subscription(
                    sub.getId(),
                    sub.getTenantId(),
                    freePlanId != null ? freePlanId : sub.getPlanId(),
                    SubscriptionStatus.EXPIRED,
                    sub.getStartedAt(),
                    sub.getEndsAt()
            );
            subscriptionRepository.save(downgraded);
            log.info("Downgraded subscription {} for tenant {}", sub.getId(), sub.getTenantId());
        }

        log.info("Downgrade job completed: {} subscriptions expired", expired.size());
    }
}
