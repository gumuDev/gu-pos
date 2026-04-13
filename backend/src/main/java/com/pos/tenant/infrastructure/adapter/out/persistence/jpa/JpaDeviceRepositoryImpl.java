package com.pos.tenant.infrastructure.adapter.out.persistence.jpa;

import com.pos.tenant.domain.model.Device;
import com.pos.tenant.domain.port.out.DeviceRepository;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.entity.DeviceEntity;
import com.pos.tenant.infrastructure.adapter.out.persistence.jpa.repository.JpaDeviceRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class JpaDeviceRepositoryImpl implements DeviceRepository {
    
    private final JpaDeviceRepository jpa;

    public JpaDeviceRepositoryImpl(JpaDeviceRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Device save(Device device) {
        DeviceEntity entity = new DeviceEntity();
        entity.setId(device.id());
        entity.setUserId(device.userId());
        entity.setDeviceFingerprint(device.deviceFingerprint());
        entity.setLastLogin(device.lastLogin());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        jpa.save(entity);
        return device;
    }

    @Override
    public Optional<Device> findByUserIdAndFingerprint(UUID userId, String fingerprint) {
        return jpa.findByUserIdAndDeviceFingerprint(userId, fingerprint)
                .map(entity -> new Device(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getDeviceFingerprint(),
                        entity.getLastLogin()
                ));
    }
}
