package com.pos.tenant.infrastructure.adapter.out.persistence.memory;

import com.pos.tenant.domain.model.Session;
import com.pos.tenant.domain.port.out.SessionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemorySessionRepository implements SessionRepository {

    private final Map<UUID, Session> store = new HashMap<>();

    @Override
    public Session save(Session session) {
        store.put(session.id(), session);
        return session;
    }

    @Override
    public Optional<Session> findByRefreshTokenHash(String hash) {
        return store.values().stream()
                .filter(s -> s.refreshTokenHash().equals(hash))
                .findFirst();
    }
}
