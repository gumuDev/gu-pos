package com.pos.tenant.domain.port.in;

public interface RegisterTenantUseCase {
    RegisterTenantResult register(RegisterTenantCommand command);
}
