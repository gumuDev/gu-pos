package com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository;

import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface JpaDeviceRepository extends JpaRepository<DeviceEntity, UUID> {
    Optional<DeviceEntity> findByUserIdAndDeviceFingerprint(UUID userId, String deviceFingerprint);
}
