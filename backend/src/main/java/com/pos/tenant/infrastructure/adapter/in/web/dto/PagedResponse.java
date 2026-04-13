package com.pos.tenant.infrastructure.adapter.in.web.dto;

import java.util.List;

public record PagedResponse<T>(List<T> data, long total, int page, int size) {}
