package com.pos.tenant.infrastructure.adapter.in.web.dto;

public record FeatureFlagsResponse(
        boolean sizeVariants,
        boolean modifiers,
        boolean combos,
        boolean stockControl,
        boolean ingredientRecipes,
        boolean customerDisplay,
        boolean ticketPrinting
) {}
