package com.pos.tenant.domain.port.in;

import com.pos.tenant.domain.model.PageResult;

public interface ListTenantsUseCase {
    PageResult<TenantSummary> list(ListTenantsQuery query);
}
