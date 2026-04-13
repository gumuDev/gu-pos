package com.pos.tenant.domain.port.in;

public interface RegisterAccountUseCase {
    AuthTokenResult register(RegisterAccountCommand command);
}
