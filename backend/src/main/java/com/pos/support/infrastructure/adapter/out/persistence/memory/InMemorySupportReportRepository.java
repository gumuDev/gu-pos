package com.pos.support.infrastructure.adapter.out.persistence.memory;

import com.pos.support.domain.model.SupportReport;
import com.pos.support.domain.port.out.SupportReportRepository;

import java.util.*;

public class InMemorySupportReportRepository implements SupportReportRepository {

    private final List<SupportReport> store = new ArrayList<>();

    @Override
    public void save(SupportReport report) {
        store.add(report);
    }

    @Override
    public List<SupportReport> findAll(int page, int size) {
        return store.stream()
                .sorted(Comparator.comparing(SupportReport::getCreatedAt).reversed())
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    @Override
    public Optional<SupportReport> findById(UUID id) {
        return store.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    @Override
    public void update(SupportReport report) {
        // already mutated in place via markReviewed()
    }
}
