package com.pos.tenant.domain.port.out;

import com.pos.tenant.domain.model.Subscription;
import com.pos.tenant.domain.model.SubscriptionHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    void saveHistory(SubscriptionHistory history);
    Optional<Subscription> findByTenantId(UUID tenantId);
    Optional<Subscription> findActiveByTenantId(UUID tenantId);
    List<Subscription> findExpiredActive();
    List<SubscriptionHistory> findHistoryByTenantId(UUID tenantId);
}
