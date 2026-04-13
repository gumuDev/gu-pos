package com.pos.tenant.domain.port.in;

import java.util.UUID;

public interface CancelSubscriptionUseCase {
    void cancel(UUID tenantId);
}
