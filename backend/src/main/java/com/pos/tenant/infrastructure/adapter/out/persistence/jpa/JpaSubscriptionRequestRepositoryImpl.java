package com.pos.tenant.infrastructure.adapter.out.persistence.jpa;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.SubscriptionRequest;
import com.pos.tenant.domain.port.out.SubscriptionRequestRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.SubscriptionRequestEntity;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaSubscriptionRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class JpaSubscriptionRequestRepositoryImpl implements SubscriptionRequestRepository {

    private final JpaSubscriptionRequestRepository jpa;

    public JpaSubscriptionRequestRepositoryImpl(JpaSubscriptionRequestRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public SubscriptionRequest save(SubscriptionRequest request) {
        SubscriptionRequestEntity entity = jpa.findById(request.getId()).orElse(new SubscriptionRequestEntity());
        entity.setId(request.getId());
        entity.setTenantId(request.getTenantId());
        entity.setPlanName(request.getPlanName());
        entity.setTransactionRef(request.getTransactionRef());
        entity.setReceiptUrl(request.getReceiptUrl());
        entity.setStatus(request.getStatus());
        entity.setCreatedAt(request.getCreatedAt() != null ? request.getCreatedAt() : Instant.now());
        entity.setUpdatedAt(Instant.now());
        jpa.save(entity);
        return request;
    }

    @Override
    public Optional<SubscriptionRequest> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<SubscriptionRequest> findPendingByTenantId(UUID tenantId) {
        return jpa.findFirstByTenantIdAndStatusOrderByCreatedAtDesc(tenantId, "pending")
                .map(this::toDomain);
    }

    @Override
    public Optional<SubscriptionRequest> findLatestByTenantId(UUID tenantId) {
        return jpa.findFirstByTenantIdOrderByCreatedAtDesc(tenantId)
                .map(this::toDomain);
    }

    @Override
    public PageResult<SubscriptionRequest> findAll(int page, int size) {
        Page<SubscriptionRequestEntity> result = jpa.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        return new PageResult<>(
                result.getContent().stream().map(this::toDomain).toList(),
                result.getTotalElements(),
                page,
                size
        );
    }

    private SubscriptionRequest toDomain(SubscriptionRequestEntity e) {
        return new SubscriptionRequest(
                e.getId(), e.getTenantId(), e.getPlanName(),
                e.getTransactionRef(), e.getReceiptUrl(),
                e.getStatus(), e.getCreatedAt()
        );
    }
}
