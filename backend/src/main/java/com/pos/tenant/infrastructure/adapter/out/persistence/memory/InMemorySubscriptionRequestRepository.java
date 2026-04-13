package com.pos.tenant.infrastructure.adapter.out.persistence.memory;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.SubscriptionRequest;
import com.pos.tenant.domain.port.out.SubscriptionRequestRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemorySubscriptionRequestRepository implements SubscriptionRequestRepository {

    private final Map<UUID, SubscriptionRequest> store = new HashMap<>();

    @Override
    public SubscriptionRequest save(SubscriptionRequest request) {
        store.put(request.getId(), request);
        return request;
    }

    @Override
    public Optional<SubscriptionRequest> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<SubscriptionRequest> findPendingByTenantId(UUID tenantId) {
        return store.values().stream()
                .filter(r -> r.getTenantId().equals(tenantId) && "pending".equals(r.getStatus()))
                .findFirst();
    }

    @Override
    public Optional<SubscriptionRequest> findLatestByTenantId(UUID tenantId) {
        return store.values().stream()
                .filter(r -> r.getTenantId().equals(tenantId))
                .max(Comparator.comparing(SubscriptionRequest::getCreatedAt));
    }

    @Override
    public PageResult<SubscriptionRequest> findAll(int page, int size) {
        List<SubscriptionRequest> sorted = store.values().stream()
                .sorted(Comparator.comparing(SubscriptionRequest::getCreatedAt).reversed())
                .toList();
        int from = Math.min(page * size, sorted.size());
        int to = Math.min(from + size, sorted.size());
        return new PageResult<>(new ArrayList<>(sorted.subList(from, to)), sorted.size(), page, size);
    }
}
