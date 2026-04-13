package com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository;

import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaTenantRepository extends JpaRepository<TenantEntity, UUID> {
    boolean existsByPhone(String phone);
    Optional<TenantEntity> findByPhone(String phone);
}
