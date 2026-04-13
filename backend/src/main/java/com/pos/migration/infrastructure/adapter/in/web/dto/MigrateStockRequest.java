package com.pos.migration.infrastructure.adapter.in.web.dto;

import java.util.List;
import java.util.UUID;

public record MigrateStockRequest(
        UUID tenantId,
        List<ProductStockDto> productStock,
        List<VariantStockDto> variantStock
) {
    public record ProductStockDto(UUID productId, String productName, int stock) {}

    public record VariantStockDto(UUID variantId, int stock) {}
}
