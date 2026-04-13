package com.pos.tenant.domain.port.in;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.SubscriptionRequest;

public interface ListSubscriptionRequestsUseCase {
    PageResult<SubscriptionRequest> list(ListTenantsQuery query);
}
