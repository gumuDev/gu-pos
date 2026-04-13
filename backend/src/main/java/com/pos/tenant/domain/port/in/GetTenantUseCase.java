package com.pos.tenant.domain.port.in;

public interface GetTenantUseCase {
    TenantDetail get(GetTenantQuery query);
}
