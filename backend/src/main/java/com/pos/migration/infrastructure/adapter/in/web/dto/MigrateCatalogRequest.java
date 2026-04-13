package com.pos.migration.infrastructure.adapter.in.web.dto;

import java.util.List;
import java.util.UUID;

public record MigrateCatalogRequest(
        UUID tenantId,
        UUID branchId,
        List<CategoryDto> categories,
        List<ProductDto> products,
        List<ProductVariantDto> variants,
        List<ModifierGroupDto> modifierGroups,
        List<ModifierOptionDto> modifierOptions,
        List<ComboDto> combos,
        List<ComboItemDto> comboItems
) {
    public record CategoryDto(UUID id, String name, String color, int sortOrder) {}

    public record ProductDto(UUID id, UUID categoryId, String name, String imageEmoji,
                             double basePrice, int stock, boolean isActive) {}

    public record ProductVariantDto(UUID id, UUID productId, String name, double price,
                                    int stock, int sortOrder, boolean isActive) {}

    public record ModifierGroupDto(UUID id, UUID productId, String name,
                                   boolean required, int sortOrder) {}

    public record ModifierOptionDto(UUID id, UUID groupId, String name,
                                    double priceDelta, int sortOrder) {}

    public record ComboDto(UUID id, String name, String description, double price,
                           String imageEmoji, boolean isActive) {}

    public record ComboItemDto(UUID id, UUID comboId, UUID productId,
                               String productName, int quantity) {}
}
