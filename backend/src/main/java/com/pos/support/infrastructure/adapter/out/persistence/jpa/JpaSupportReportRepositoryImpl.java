package com.pos.support.infrastructure.adapter.out.persistence.jpa;

import com.pos.support.domain.model.SupportReport;
import com.pos.support.domain.port.out.SupportReportRepository;
import com.pos.support.infrastructure.adapter.out.persistence.jpa.entity.SupportReportEntity;
import com.pos.support.infrastructure.adapter.out.persistence.jpa.repository.JpaSupportReportRepository;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JpaSupportReportRepositoryImpl implements SupportReportRepository {

    private final JpaSupportReportRepository jpa;

    public JpaSupportReportRepositoryImpl(JpaSupportReportRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(SupportReport report) {
        jpa.save(toEntity(report));
    }

    @Override
    public List<SupportReport> findAll(int page, int size) {
        return jpa.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
                .stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<SupportReport> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public void update(SupportReport report) {
        jpa.save(toEntity(report));
    }

    private SupportReportEntity toEntity(SupportReport r) {
        SupportReportEntity e = new SupportReportEntity();
        e.setId(r.getId());
        e.setTenantId(r.getTenantId());
        e.setType(r.getType());
        e.setDescription(r.getDescription());
        e.setScreenshotUrl(r.getScreenshotUrl());
        e.setStatus(r.getStatus());
        e.setCreatedAt(r.getCreatedAt());
        return e;
    }

    private SupportReport toDomain(SupportReportEntity e) {
        return new SupportReport(e.getId(), e.getTenantId(), e.getType(),
                e.getDescription(), e.getScreenshotUrl(), e.getStatus(), e.getCreatedAt());
    }
}
