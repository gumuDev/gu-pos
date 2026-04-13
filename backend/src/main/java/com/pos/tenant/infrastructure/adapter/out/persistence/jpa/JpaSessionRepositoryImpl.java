package com.pos.tenant.infrastructure.adapter.out.persistence.jpa;

import com.pos.tenant.domain.model.Session;
import com.pos.tenant.domain.port.out.SessionRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.SessionEntity;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaSessionRepository;

import java.time.Instant;
import java.util.Optional;

public class JpaSessionRepositoryImpl implements SessionRepository {

    private final JpaSessionRepository jpa;

    public JpaSessionRepositoryImpl(JpaSessionRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Session save(Session session) {
        SessionEntity entity = new SessionEntity();
        entity.setId(session.id());
        entity.setUserId(session.userId());
        entity.setDeviceId(session.deviceId());
        entity.setRefreshTokenHash(session.refreshTokenHash());
        entity.setRevoked(session.isRevoked());
        entity.setExpiresAt(session.expiresAt());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        jpa.save(entity);
        return session;
    }

    @Override
    public Optional<Session> findByRefreshTokenHash(String hash) {
        return jpa.findByRefreshTokenHash(hash)
                .map(entity -> new Session(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getDeviceId(),
                        entity.getRefreshTokenHash(),
                        entity.isRevoked(),
                        entity.getExpiresAt()
                ));
    }
}
