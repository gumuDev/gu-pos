package com.pos.tenant.infrastructure.adapter.out.persistence.memory;

import com.pos.tenant.domain.model.User;
import com.pos.tenant.domain.port.out.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> store = new HashMap<>();

    @Override
    public User save(User user) {
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public int countCashiersByTenantId(UUID tenantId) {
        return (int) store.values().stream()
                .filter(u -> u.getTenantId().equals(tenantId) && "cashier".equals(u.getRole()))
                .count();
    }

    @Override
    public java.util.Optional<User> findByPhone(String phone) {
        return store.values().stream()
                .filter(u -> u.getPhone().equals(phone))
                .findFirst();
    }

    @Override
    public java.util.Optional<User> findById(UUID id) {
        return java.util.Optional.ofNullable(store.get(id));
    }
}
