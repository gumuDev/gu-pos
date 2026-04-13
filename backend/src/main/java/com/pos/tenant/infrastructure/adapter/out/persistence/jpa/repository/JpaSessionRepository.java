package com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository;

import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface JpaSessionRepository extends JpaRepository<SessionEntity, UUID> {
    Optional<SessionEntity> findByRefreshTokenHash(String refreshTokenHash);
}
