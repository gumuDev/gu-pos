package com.pos.tenant.domain.port.out;

import com.pos.tenant.domain.model.User;

import java.util.UUID;

public interface UserRepository {
    User save(User user);
    int countCashiersByTenantId(UUID tenantId);
    java.util.Optional<User> findByPhone(String phone);
    java.util.Optional<User> findById(UUID id);
}
