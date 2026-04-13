package com.pos.tenant.domain.port.in;

import java.util.UUID;

public interface RequestSubscriptionUseCase {
    UUID request(RequestSubscriptionCommand command);
}
