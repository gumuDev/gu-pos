package com.pos.tenant.infrastructure.adapter.out.persistence.jpa;

import com.pos.tenant.domain.model.Subscription;
import com.pos.tenant.domain.model.SubscriptionHistory;
import com.pos.tenant.domain.model.SubscriptionStatus;
import com.pos.tenant.domain.port.out.SubscriptionRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.SubscriptionEntity;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.SubscriptionHistoryEntity;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaSubscriptionHistoryRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaSubscriptionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JpaSubscriptionRepositoryImpl implements SubscriptionRepository {

    private final JpaSubscriptionRepository jpa;
    private final JpaSubscriptionHistoryRepository jpaHistory;

    public JpaSubscriptionRepositoryImpl(JpaSubscriptionRepository jpa,
                                         JpaSubscriptionHistoryRepository jpaHistory) {
        this.jpa = jpa;
        this.jpaHistory = jpaHistory;
    }

    @Override
    public Subscription save(Subscription subscription) {
        // Upsert — buscar registro existente del tenant o crear uno nuevo
        SubscriptionEntity entity = jpa.findByTenantId(subscription.getTenantId())
                .orElse(new SubscriptionEntity());

        entity.setId(entity.getId() != null ? entity.getId() : subscription.getId());
        entity.setTenantId(subscription.getTenantId());
        entity.setPlanId(subscription.getPlanId());
        entity.setStatus(subscription.getStatus().toValue());
        entity.setStartedAt(subscription.getStartedAt());
        entity.setEndsAt(subscription.getEndsAt());
        entity.setUpdatedAt(Instant.now());
        if (entity.getCreatedAt() == null) entity.setCreatedAt(Instant.now());

        jpa.save(entity);
        return subscription;
    }

    @Override
    public void saveHistory(SubscriptionHistory history) {
        SubscriptionHistoryEntity entity = new SubscriptionHistoryEntity();
        entity.setId(history.getId());
        entity.setTenantId(history.getTenantId());
        entity.setPlanId(history.getPlanId());
        entity.setStatus(history.getStatus());
        entity.setStartedAt(history.getStartedAt());
        entity.setEndsAt(history.getEndsAt());
        entity.setActivatedAt(history.getActivatedAt());
        entity.setCreatedAt(Instant.now());
        jpaHistory.save(entity);
    }

    @Override
    public Optional<Subscription> findByTenantId(UUID tenantId) {
        return jpa.findByTenantId(tenantId).map(this::toDomain);
    }

    @Override
    public Optional<Subscription> findActiveByTenantId(UUID tenantId) {
        return jpa.findActiveByTenantId(tenantId).map(this::toDomain);
    }

    @Override
    public List<Subscription> findExpiredActive() {
        return jpa.findExpiredActive(Instant.now()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<SubscriptionHistory> findHistoryByTenantId(UUID tenantId) {
        return jpaHistory.findByTenantIdOrderByActivatedAtDesc(tenantId).stream()
                .map(e -> new SubscriptionHistory(e.getId(), e.getTenantId(), e.getPlanId(),
                        e.getStatus(), e.getStartedAt(), e.getEndsAt(), e.getActivatedAt()))
                .toList();
    }

    private Subscription toDomain(SubscriptionEntity e) {
        return new Subscription(e.getId(), e.getTenantId(), e.getPlanId(),
                SubscriptionStatus.from(e.getStatus()), e.getStartedAt(), e.getEndsAt());
    }
}
