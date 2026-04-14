CREATE TABLE mobile_error_logs (
    id           UUID        PRIMARY KEY,
    tenant_id    UUID,
    user_id      UUID,
    session_id   UUID,
    level        VARCHAR(16) NOT NULL,
    message      TEXT        NOT NULL,
    stack        TEXT,
    screen       VARCHAR(120),
    action       VARCHAR(120),
    platform     VARCHAR(16) NOT NULL,
    os_version   VARCHAR(32),
    device_model VARCHAR(80),
    app_version  VARCHAR(32) NOT NULL,
    build_number VARCHAR(32),
    occurred_at  TIMESTAMPTZ NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_mobile_error_logs_tenant_created
    ON mobile_error_logs (tenant_id, created_at DESC);

CREATE INDEX idx_mobile_error_logs_tenant_level_created
    ON mobile_error_logs (tenant_id, level, created_at DESC);

CREATE INDEX idx_mobile_error_logs_tenant_version_created
    ON mobile_error_logs (tenant_id, app_version, created_at DESC);
