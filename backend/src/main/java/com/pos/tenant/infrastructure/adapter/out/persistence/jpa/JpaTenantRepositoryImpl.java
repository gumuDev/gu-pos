package com.pos.tenant.infrastructure.adapter.out.persistence.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.tenant.domain.model.FeatureFlags;
import com.pos.tenant.domain.model.PageResult;
import com.pos.tenant.domain.model.Tenant;
import com.pos.tenant.domain.port.out.TenantRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.TenantEntity;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaTenantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JpaTenantRepositoryImpl implements TenantRepository {

    private final JpaTenantRepository jpa;
    private final ObjectMapper objectMapper;

    public JpaTenantRepositoryImpl(JpaTenantRepository jpa, ObjectMapper objectMapper) {
        this.jpa = jpa;
        this.objectMapper = objectMapper;
    }

    @Override
    public Tenant save(Tenant tenant) {
        TenantEntity entity = new TenantEntity();
        entity.setId(tenant.getId());
        entity.setName(tenant.getName());
        entity.setPhone(tenant.getPhone());
        entity.setCurrency(tenant.getCurrency());
        entity.setBusinessType(tenant.getBusinessType());
        entity.setPlanId(tenant.getPlanId());
        entity.setStatus(tenant.getStatus());
        entity.setCreatedAt(tenant.getCreatedAt());
        entity.setUpdatedAt(Instant.now());

        try {
            entity.setFeaturesConfig(objectMapper.writeValueAsString(tenant.getFeaturesConfig()));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("failed to serialize features_config", e);
        }

        jpa.save(entity);
        return tenant;
    }

    @Override
    public boolean existsByPhone(String phone) {
        return jpa.existsByPhone(phone);
    }

    @Override
    public PageResult<Tenant> findAll(int page, int size) {
        Page<TenantEntity> result = jpa.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        List<Tenant> tenants = result.getContent().stream().map(this::toDomain).toList();
        return new PageResult<>(tenants, result.getTotalElements(), page, size);
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Tenant> findByPhone(String phone) {
        return jpa.findByPhone(phone).map(this::toDomain);
    }

    @Override
    public void saveTelegramChatId(UUID tenantId, Long chatId) {
        jpa.findById(tenantId).ifPresent(entity -> {
            entity.setTelegramChatId(chatId);
            jpa.save(entity);
        });
    }

    @Override
    public void clearTelegramChatId(UUID tenantId) {
        jpa.findById(tenantId).ifPresent(entity -> {
            entity.setTelegramChatId(null);
            jpa.save(entity);
        });
    }

    private Tenant toDomain(TenantEntity entity) {
        FeatureFlags features;
        try {
            features = objectMapper.readValue(entity.getFeaturesConfig(), FeatureFlags.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("failed to deserialize features_config", e);
        }
        return new Tenant(
                entity.getId(),
                entity.getName(),
                entity.getPhone(),
                entity.getCurrency(),
                entity.getBusinessType(),
                features,
                entity.getPlanId(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getTelegramChatId()
        );
    }
}
