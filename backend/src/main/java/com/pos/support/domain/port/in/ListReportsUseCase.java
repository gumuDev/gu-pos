package com.pos.support.domain.port.in;

import com.pos.support.domain.model.SupportReport;
import java.util.List;

public interface ListReportsUseCase {
    List<SupportReport> listAll(int page, int size);
    void markReviewed(java.util.UUID reportId);
}
