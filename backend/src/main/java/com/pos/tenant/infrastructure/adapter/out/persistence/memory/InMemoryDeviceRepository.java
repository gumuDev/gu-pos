package com.pos.tenant.infrastructure.adapter.out.persistence.memory;

import com.pos.tenant.domain.model.Device;
import com.pos.tenant.domain.port.out.DeviceRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryDeviceRepository implements DeviceRepository {

    private final Map<UUID, Device> store = new HashMap<>();

    @Override
    public Device save(Device device) {
        store.put(device.id(), device);
        return device;
    }

    @Override
    public Optional<Device> findByUserIdAndFingerprint(UUID userId, String fingerprint) {
        return store.values().stream()
                .filter(d -> d.userId().equals(userId) && d.deviceFingerprint().equals(fingerprint))
                .findFirst();
    }
}
