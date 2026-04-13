package com.pos.migration.infrastructure.adapter.in.web.dto;

import java.util.List;
import java.util.UUID;

public record MigrateOrdersRequest(
        UUID tenantId,
        UUID branchId,
        List<OrderDto> orders,
        List<OrderItemDto> orderItems
) {
    public record OrderDto(UUID id, String sessionId, String cashierName, double subtotal,
                           double discountTotal, double total, String paymentMethod,
                           Double cashTendered, Double changeAmount,
                           int dailySequence, String status, long createdAt) {
        public UUID sessionUUID() {
            try { return sessionId != null ? UUID.fromString(sessionId) : null; }
            catch (IllegalArgumentException e) { return null; }
        }
    }

    public record OrderItemDto(UUID id, UUID orderId, UUID productId, String productName,
                               UUID variantId, String variantName, String modifiersJson,
                               double unitPrice, int quantity, double subtotal) {}
}
