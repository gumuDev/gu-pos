package com.pos.errorlog.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateErrorLogRequest(
    @NotNull Instant occurredAt,
    @NotBlank String level,
    @NotBlank String message,
    String stack,
    String screen,
    String action,
    @NotBlank String appVersion,
    String buildNumber,
    @NotBlank String platform,
    String osVersion,
    String deviceModel,
    UUID sessionId
) {}
