package com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository;

import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.SubscriptionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaSubscriptionHistoryRepository extends JpaRepository<SubscriptionHistoryEntity, UUID> {
    List<SubscriptionHistoryEntity> findByTenantIdOrderByActivatedAtDesc(UUID tenantId);
}
