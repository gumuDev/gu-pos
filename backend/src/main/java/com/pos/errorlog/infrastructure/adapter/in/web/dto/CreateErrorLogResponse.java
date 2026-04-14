package com.pos.errorlog.infrastructure.adapter.in.web.dto;

import java.util.UUID;

public record CreateErrorLogResponse(UUID errorId, boolean stored) {}
