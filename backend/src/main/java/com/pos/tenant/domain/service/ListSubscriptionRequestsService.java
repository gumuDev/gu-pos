package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.SubscriptionRequest;
import com.pos.tenant.domain.port.in.ListSubscriptionRequestsUseCase;
import com.pos.tenant.domain.port.in.ListTenantsQuery;
import com.pos.tenant.domain.port.out.SubscriptionRequestRepository;

public class ListSubscriptionRequestsService implements ListSubscriptionRequestsUseCase {

    private final SubscriptionRequestRepository repository;

    public ListSubscriptionRequestsService(SubscriptionRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<SubscriptionRequest> list(ListTenantsQuery query) {
        return repository.findAll(query.page(), query.size());
    }
}
