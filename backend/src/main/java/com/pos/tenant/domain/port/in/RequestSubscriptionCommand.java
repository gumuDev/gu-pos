package com.pos.tenant.domain.port.in;

import java.util.UUID;

public record RequestSubscriptionCommand(
        UUID tenantId,
        String planName,
        String transactionRef,
        String receiptUrl
) {}
