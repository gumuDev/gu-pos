package com.pos.support.domain.port.out;

import com.pos.support.domain.model.SupportReport;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupportReportRepository {
    void save(SupportReport report);
    List<SupportReport> findAll(int page, int size);
    Optional<SupportReport> findById(UUID id);
    void update(SupportReport report);
}
