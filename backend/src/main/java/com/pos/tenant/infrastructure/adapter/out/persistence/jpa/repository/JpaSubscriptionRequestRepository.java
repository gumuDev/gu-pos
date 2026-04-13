package com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository;

import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.SubscriptionRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaSubscriptionRequestRepository extends JpaRepository<SubscriptionRequestEntity, UUID> {
    Optional<SubscriptionRequestEntity> findFirstByTenantIdAndStatusOrderByCreatedAtDesc(UUID tenantId, String status);
    Optional<SubscriptionRequestEntity> findFirstByTenantIdOrderByCreatedAtDesc(UUID tenantId);
    Page<SubscriptionRequestEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
