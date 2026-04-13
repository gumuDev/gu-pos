package com.pos.tenant.domain.model;

import java.util.List;

public record PageResult<T>(List<T> data, long total, int page, int size) {}
