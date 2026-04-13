package com.pos.tenant.infrastructure.adapter.out.persistence.memory;

import com.pos.tenant.domain.model.Subscription;
import com.pos.tenant.domain.model.SubscriptionHistory;
import com.pos.tenant.domain.model.SubscriptionStatus;
import com.pos.tenant.domain.port.out.SubscriptionRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemorySubscriptionRepository implements SubscriptionRepository {

    private final Map<UUID, Subscription> store = new HashMap<>();

    @Override
    public Subscription save(Subscription subscription) {
        store.put(subscription.getId(), subscription);
        return subscription;
    }

    @Override
    public void saveHistory(SubscriptionHistory history) {
        // no-op para tests en memoria
    }

    @Override
    public Optional<Subscription> findByTenantId(UUID tenantId) {
        return store.values().stream()
                .filter(s -> s.getTenantId().equals(tenantId))
                .findFirst();
    }

    @Override
    public Optional<Subscription> findActiveByTenantId(UUID tenantId) {
        return store.values().stream()
                .filter(s -> s.getTenantId().equals(tenantId) && s.isActive())
                .findFirst();
    }

    @Override
    public List<com.pos.tenant.domain.model.SubscriptionHistory> findHistoryByTenantId(UUID tenantId) {
        return java.util.Collections.emptyList();
    }

    @Override
    public List<Subscription> findExpiredActive() {
        Instant now = Instant.now();
        return store.values().stream()
                .filter(s -> SubscriptionStatus.ACTIVE.equals(s.getStatus())
                        && s.getEndsAt() != null
                        && s.getEndsAt().isBefore(now))
                .toList();
    }
}
