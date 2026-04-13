package com.pos.tenant.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RequestSubscriptionRequest(
        @NotNull UUID tenantId,
        @NotBlank String planName,
        String transactionRef,
        @NotBlank String receiptUrl
) {}
