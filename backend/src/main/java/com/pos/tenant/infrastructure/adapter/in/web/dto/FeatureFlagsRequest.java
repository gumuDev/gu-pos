package com.pos.tenant.infrastructure.adapter.in.web.dto;

public record FeatureFlagsRequest(
        boolean sizeVariants,
        boolean modifiers,
        boolean combos,
        boolean stockControl,
        boolean ingredientRecipes,
        boolean customerDisplay,
        boolean ticketPrinting
) {}
