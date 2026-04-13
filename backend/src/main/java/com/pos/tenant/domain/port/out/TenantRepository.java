package com.pos.tenant.domain.port.out;

import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.Tenant;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository {
    Tenant save(Tenant tenant);
    boolean existsByPhone(String phone);
    PageResult<Tenant> findAll(int page, int size);
    Optional<Tenant> findById(UUID id);
    Optional<Tenant> findByPhone(String phone);
    void saveTelegramChatId(UUID tenantId, Long chatId);
    void clearTelegramChatId(UUID tenantId);
}
