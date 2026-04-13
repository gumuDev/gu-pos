package com.pos.tenant.infrastructure.adapter.in.web;

import com.pos.tenant.domain.port.in.AuthTokenResult;
import com.pos.tenant.domain.port.in.LoginCommand;
import com.pos.tenant.domain.port.in.LoginUseCase;
import com.pos.tenant.domain.port.in.RefreshTokenCommand;
import com.pos.tenant.domain.port.in.RefreshTokenUseCase;
import com.pos.tenant.domain.port.in.RegisterAccountCommand;
import com.pos.tenant.domain.port.in.RegisterAccountUseCase;
import com.pos.tenant.infrastructure.adapter.in.web.dto.AuthResponse;
import com.pos.tenant.infrastructure.adapter.in.web.dto.LoginRequest;
import com.pos.tenant.infrastructure.adapter.in.web.dto.RefreshTokenRequest;
import com.pos.tenant.infrastructure.adapter.in.web.dto.RegisterAccountRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RegisterAccountUseCase registerAccountUseCase;

    public AuthController(LoginUseCase loginUseCase,
                          RefreshTokenUseCase refreshTokenUseCase,
                          RegisterAccountUseCase registerAccountUseCase) {
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.registerAccountUseCase = registerAccountUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterAccountRequest request) {
        RegisterAccountCommand command = new RegisterAccountCommand(
                request.tenantId(), request.phone(), request.password(), request.deviceFingerprint()
        );
        AuthTokenResult result = registerAccountUseCase.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(
                result.accessToken(), result.refreshToken(), result.sessionExpiresAt(), result.tenantId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand(request.phone(), request.password(), request.deviceFingerprint());
        AuthTokenResult result = loginUseCase.login(command);

        return ResponseEntity.ok(new AuthResponse(
                result.accessToken(),
                result.refreshToken(),
                result.sessionExpiresAt(),
                result.tenantId()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenCommand command = new RefreshTokenCommand(request.refreshToken(), request.deviceFingerprint());
        AuthTokenResult result = refreshTokenUseCase.refresh(command);

        return ResponseEntity.ok(new AuthResponse(
                result.accessToken(),
                result.refreshToken(),
                result.sessionExpiresAt(),
                result.tenantId()
        ));
    }
}
