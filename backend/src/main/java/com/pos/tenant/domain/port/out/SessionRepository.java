package com.pos.tenant.domain.port.out;

import com.pos.tenant.domain.model.Session;
import java.util.Optional;

public interface SessionRepository {
    Session save(Session session);
    Optional<Session> findByRefreshTokenHash(String hash);
}
