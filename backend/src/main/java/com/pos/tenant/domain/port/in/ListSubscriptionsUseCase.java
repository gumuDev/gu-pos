package com.pos.tenant.domain.port.in;

import com.pos.tenant.domain.model.PageResult;

public interface ListSubscriptionsUseCase {
    PageResult<SubscriptionSummary> list(ListTenantsQuery query);
}
