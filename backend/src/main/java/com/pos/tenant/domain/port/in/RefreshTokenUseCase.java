package com.pos.tenant.domain.port.in;

public interface RefreshTokenUseCase {
    AuthTokenResult refresh(RefreshTokenCommand command);
}
