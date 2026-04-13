package com.pos.tenant.domain.port.in;

public interface LoginUseCase {
    AuthTokenResult login(LoginCommand command);
}
