package com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository;

import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaBranchRepository extends JpaRepository<BranchEntity, UUID> {
    Optional<BranchEntity> findByTenantId(UUID tenantId);
}
