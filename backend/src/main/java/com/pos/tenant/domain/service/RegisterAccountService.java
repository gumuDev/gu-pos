package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.Device;
import com.pos.tenant.domain.model.Session;
import com.pos.tenant.domain.model.User;
import com.pos.tenant.domain.port.in.AuthTokenResult;
import com.pos.tenant.domain.port.in.RegisterAccountCommand;
import com.pos.tenant.domain.port.in.RegisterAccountUseCase;
import com.pos.tenant.domain.port.out.AuthTokenPort;
import com.pos.tenant.domain.port.out.DeviceRepository;
import com.pos.tenant.domain.port.out.SessionRepository;
import com.pos.tenant.domain.port.out.SubscriptionRepository;
import com.pos.tenant.domain.port.out.TenantRepository;
import com.pos.tenant.domain.port.out.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class RegisterAccountService implements RegisterAccountUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegisterAccountService.class);

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final DeviceRepository deviceRepository;
    private final SessionRepository sessionRepository;
    private final AuthTokenPort authTokenPort;

    public RegisterAccountService(TenantRepository tenantRepository,
                                   UserRepository userRepository,
                                   SubscriptionRepository subscriptionRepository,
                                   DeviceRepository deviceRepository,
                                   SessionRepository sessionRepository,
                                   AuthTokenPort authTokenPort) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.deviceRepository = deviceRepository;
        this.sessionRepository = sessionRepository;
        this.authTokenPort = authTokenPort;
    }

    @Override
    public AuthTokenResult register(RegisterAccountCommand command) {
        log.info("Registering cloud account — tenantId={} phone={}", command.tenantId(), command.phone());

        var tenant = tenantRepository.findById(command.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("tenant_not_found"));

        var subscription = subscriptionRepository.findByTenantId(command.tenantId())
                .orElseThrow(() -> new IllegalStateException("no_active_plan"));

        if (!subscription.isActive()) {
            throw new IllegalStateException("plan_not_active");
        }

        if (userRepository.findByPhone(command.phone()).isPresent()) {
            throw new IllegalArgumentException("phone_already_registered");
        }

        User user = userRepository.save(new User(
                UUID.randomUUID(),
                command.tenantId(),
                command.phone(),
                authTokenPort.hashPassword(command.password()),
                "business_admin",
                tenant.getName()
        ));

        Device device = deviceRepository.save(
                new Device(UUID.randomUUID(), user.getId(), command.deviceFingerprint(), Instant.now())
        );

        String accessToken = authTokenPort.generateAccessToken(user.getId(), command.tenantId());
        String refreshToken = authTokenPort.generateRefreshToken(user.getId());
        Instant expiresAt = Instant.now().plus(30, ChronoUnit.DAYS);

        sessionRepository.save(new Session(
                UUID.randomUUID(), user.getId(), device.id(),
                authTokenPort.hashToken(refreshToken), false, expiresAt
        ));

        log.info("Cloud account created — userId={} tenantId={}", user.getId(), command.tenantId());

        return new AuthTokenResult(accessToken, refreshToken, expiresAt, command.tenantId());
    }
}
