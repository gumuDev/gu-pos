package com.pos.support.infrastructure.adapter.out.persistence.jpa.repository;

import com.pos.support.infrastructure.adapter.out.persistence.jpa.entity.SupportReportEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaSupportReportRepository extends JpaRepository<SupportReportEntity, UUID> {
    List<SupportReportEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
