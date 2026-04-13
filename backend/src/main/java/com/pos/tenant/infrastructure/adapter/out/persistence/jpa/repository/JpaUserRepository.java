package com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository;

import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    int countByTenantIdAndRoleAndDeletedAtIsNull(UUID tenantId, String role);
    java.util.Optional<UserEntity> findByPhone(String phone);
}
