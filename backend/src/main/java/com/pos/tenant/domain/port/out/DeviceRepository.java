package com.pos.tenant.domain.port.out;

import com.pos.tenant.domain.model.Device;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository {
    Device save(Device device);
    Optional<Device> findByUserIdAndFingerprint(UUID userId, String fingerprint);
}
