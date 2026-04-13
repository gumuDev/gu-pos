package com.pos.tenant.domain.model;

public record FeatureFlags(
        boolean sizeVariants,
        boolean modifiers,
        boolean combos,
        boolean stockControl,
        boolean ingredientRecipes,
        boolean customerDisplay,
        boolean ticketPrinting
) {}
