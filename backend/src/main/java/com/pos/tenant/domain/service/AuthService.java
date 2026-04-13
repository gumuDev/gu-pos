package com.pos.tenant.domain.service;

import com.pos.tenant.domain.model.Device;
import com.pos.tenant.domain.model.Session;
import com.pos.tenant.domain.model.User;
import com.pos.tenant.domain.port.in.AuthTokenResult;
import com.pos.tenant.domain.port.in.LoginCommand;
import com.pos.tenant.domain.port.in.LoginUseCase;
import com.pos.tenant.domain.port.in.RefreshTokenCommand;
import com.pos.tenant.domain.port.in.RefreshTokenUseCase;
import com.pos.tenant.domain.port.out.AuthTokenPort;
import com.pos.tenant.domain.port.out.DeviceRepository;
import com.pos.tenant.domain.port.out.SessionRepository;
import com.pos.tenant.domain.port.out.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class AuthService implements LoginUseCase, RefreshTokenUseCase {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final SessionRepository sessionRepository;
    private final AuthTokenPort authTokenPort;

    public AuthService(UserRepository userRepository,
                       DeviceRepository deviceRepository,
                       SessionRepository sessionRepository,
                       AuthTokenPort authTokenPort) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.sessionRepository = sessionRepository;
        this.authTokenPort = authTokenPort;
    }

    @Override
    public AuthTokenResult login(LoginCommand command) {
        log.debug("Login attempt for phone={}", command.phone());

        User user = userRepository.findByPhone(command.phone())
                .orElseThrow(() -> new IllegalArgumentException("invalid_credentials"));

        if (!authTokenPort.verifyPassword(command.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("invalid_credentials");
        }

        Device device = deviceRepository.findByUserIdAndFingerprint(user.getId(), command.deviceFingerprint())
                .orElseGet(() -> {
                    Device newDevice = new Device(UUID.randomUUID(), user.getId(), command.deviceFingerprint(), Instant.now());
                    return deviceRepository.save(newDevice);
                });

        if (device.id() != null) {
            device = deviceRepository.save(new Device(device.id(), user.getId(), command.deviceFingerprint(), Instant.now()));
        }

        String accessToken = authTokenPort.generateAccessToken(user.getId(), user.getTenantId());
        String refreshToken = authTokenPort.generateRefreshToken(user.getId());

        String hashedRefresh = authTokenPort.hashToken(refreshToken);

        Instant expiresAt = Instant.now().plus(30, ChronoUnit.DAYS);

        Session session = new Session(UUID.randomUUID(), user.getId(), device.id(), hashedRefresh, false, expiresAt);
        sessionRepository.save(session);

        return new AuthTokenResult(accessToken, refreshToken, expiresAt, user.getTenantId());
    }

    @Override
    public AuthTokenResult refresh(RefreshTokenCommand command) {
        UUID userId = authTokenPort.extractUserIdFromRefresh(command.refreshToken());
        
        String hashedRefresh = authTokenPort.hashToken(command.refreshToken());
        Session session = sessionRepository.findByRefreshTokenHash(hashedRefresh)
                .orElseThrow(() -> new IllegalArgumentException("invalid_refresh_token"));

        if (session.isRevoked() || session.isExpired()) {
            throw new IllegalArgumentException("refresh_token_expired_or_revoked");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user_not_found"));

        String accessToken = authTokenPort.generateAccessToken(user.getId(), user.getTenantId());
        String newRefreshToken = authTokenPort.generateRefreshToken(user.getId());
        String newHashedRefresh = authTokenPort.hashToken(newRefreshToken);

        Instant expiresAt = Instant.now().plus(30, ChronoUnit.DAYS);

        Session newSession = new Session(session.id(), user.getId(), session.deviceId(), newHashedRefresh, false, expiresAt);
        sessionRepository.save(newSession);

        return new AuthTokenResult(accessToken, newRefreshToken, expiresAt, user.getTenantId());
    }
}
