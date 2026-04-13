package com.pos.tenant.infrastructure.adapter.out.persistence.memory;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.Tenant;
import com.pos.tenant.domain.port.out.TenantRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryTenantRepository implements TenantRepository {

    private final Map<UUID, Tenant> store = new HashMap<>();

    @Override
    public Tenant save(Tenant tenant) {
        store.put(tenant.getId(), tenant);
        return tenant;
    }

    @Override
    public boolean existsByPhone(String phone) {
        return store.values().stream()
                .anyMatch(t -> t.getPhone().equals(phone));
    }

    @Override
    public PageResult<Tenant> findAll(int page, int size) {
        List<Tenant> all = store.values().stream()
                .sorted(Comparator.comparing(Tenant::getCreatedAt).reversed())
                .toList();
        List<Tenant> slice = all.stream().skip((long) page * size).limit(size).toList();
        return new PageResult<>(slice, all.size(), page, size);
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Tenant> findByPhone(String phone) {
        return store.values().stream()
                .filter(t -> t.getPhone().equals(phone))
                .findFirst();
    }

    @Override
    public void saveTelegramChatId(UUID tenantId, Long chatId) {
        Tenant existing = store.get(tenantId);
        if (existing != null) {
            store.put(tenantId, new Tenant(
                    existing.getId(), existing.getName(), existing.getPhone(),
                    existing.getCurrency(), existing.getBusinessType(),
                    existing.getFeaturesConfig(), existing.getPlanId(),
                    existing.getStatus(), existing.getCreatedAt(), chatId
            ));
        }
    }

    @Override
    public void clearTelegramChatId(UUID tenantId) {
        saveTelegramChatId(tenantId, null);
    }
}
