package com.pos.tenant.domain.port.out;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.SubscriptionRequest;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRequestRepository {
    SubscriptionRequest save(SubscriptionRequest request);
    Optional<SubscriptionRequest> findById(UUID id);
    Optional<SubscriptionRequest> findPendingByTenantId(UUID tenantId);
    Optional<SubscriptionRequest> findLatestByTenantId(UUID tenantId);
    PageResult<SubscriptionRequest> findAll(int page, int size);
}
