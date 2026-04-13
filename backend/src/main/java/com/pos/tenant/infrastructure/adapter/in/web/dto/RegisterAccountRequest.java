package com.pos.tenant.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RegisterAccountRequest(
        @NotNull
        UUID tenantId,

        @NotBlank
        @Size(min = 7, max = 15)
        String phone,

        @NotBlank
        @Size(min = 6, max = 50)
        String password,

        @NotBlank
        String deviceFingerprint
) {}
