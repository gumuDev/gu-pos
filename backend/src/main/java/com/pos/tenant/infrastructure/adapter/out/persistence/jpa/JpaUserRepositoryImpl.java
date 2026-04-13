package com.pos.tenant.infrastructure.adapter.out.persistence.jpa;

import com.pos.tenant.domain.model.User;
import com.pos.tenant.domain.port.out.UserRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.UserEntity;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaUserRepository;

import java.time.Instant;
import java.util.UUID;

public class JpaUserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpa;

    public JpaUserRepositoryImpl(JpaUserRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public int countCashiersByTenantId(UUID tenantId) {
        return jpa.countByTenantIdAndRoleAndDeletedAtIsNull(tenantId, "cashier");
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setTenantId(user.getTenantId());
        entity.setPhone(user.getPhone());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setRole(user.getRole());
        entity.setName(user.getName());
        entity.setActive(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        jpa.save(entity);
        return user;
    }

    @Override
    public java.util.Optional<User> findByPhone(String phone) {
        return jpa.findByPhone(phone).map(this::toDomain);
    }

    @Override
    public java.util.Optional<User> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getTenantId(),
                entity.getPhone(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.getName()
        );
    }
}
