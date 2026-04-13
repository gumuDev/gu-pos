package com.pos.tenant.infrastructure.adapter.in.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterTenantRequest(

        @NotBlank
        @Size(min = 2, max = 60)
        String businessName,

        @NotBlank
        @Size(min = 7, max = 15)
        String phone,

        @NotBlank
        @Pattern(regexp = "BOB|USD", message = "currency must be BOB or USD")
        String currency,

        @NotBlank
        @Pattern(regexp = "restaurant|pizzeria|bakery|retail|other",
                 message = "invalid businessType")
        String businessType,

        @NotNull
        @Valid
        FeatureFlagsRequest features
) {}
