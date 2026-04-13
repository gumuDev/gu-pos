package com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository;

import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {
    Optional<SubscriptionEntity> findByTenantId(UUID tenantId);

    @Query("SELECT s FROM SubscriptionEntity s WHERE s.tenantId = :tenantId AND s.status = 'active'")
    Optional<SubscriptionEntity> findActiveByTenantId(@Param("tenantId") UUID tenantId);

    @Query("SELECT s FROM SubscriptionEntity s WHERE s.status = 'active' AND s.endsAt IS NOT NULL AND s.endsAt <= :now")
    List<SubscriptionEntity> findExpiredActive(@Param("now") Instant now);
}
