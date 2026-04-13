package com.pos.support.domain.service;

import com.pos.support.domain.model.SupportReport;
import com.pos.support.domain.port.in.ListReportsUseCase;
import com.pos.support.domain.port.out.SupportReportRepository;

import java.util.List;
import java.util.UUID;

public class ListReportsService implements ListReportsUseCase {

    private final SupportReportRepository repository;

    public ListReportsService(SupportReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SupportReport> listAll(int page, int size) {
        return repository.findAll(page, size);
    }

    @Override
    public void markReviewed(UUID reportId) {
        repository.findById(reportId).ifPresent(report -> {
            report.markReviewed();
            repository.update(report);
        });
    }
}
