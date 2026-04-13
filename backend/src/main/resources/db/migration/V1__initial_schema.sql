-- ============================================================
-- V1__initial_schema.sql
-- Schema inicial adaptado para auth por teléfono (sin email)
-- ============================================================

-- ============================================================
-- SAAS — Nivel plataforma
-- ============================================================

CREATE TABLE saas_plans (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name         TEXT NOT NULL,
    price_usd    NUMERIC(10,2) NOT NULL DEFAULT 0,
    max_branches INT,
    max_cashiers INT,
    features     JSONB NOT NULL DEFAULT '{}',
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE saas_tenants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            TEXT NOT NULL,
    phone           TEXT NOT NULL UNIQUE,
    currency        TEXT NOT NULL DEFAULT 'BOB',
    business_type   TEXT NOT NULL DEFAULT 'other',
    features_config JSONB NOT NULL DEFAULT '{}',
    plan_id         UUID NOT NULL REFERENCES saas_plans(id),
    status          TEXT NOT NULL DEFAULT 'active',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE saas_subscriptions (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id     UUID NOT NULL REFERENCES saas_tenants(id),
    plan_id       UUID NOT NULL REFERENCES saas_plans(id),
    stripe_sub_id TEXT,
    status        TEXT NOT NULL DEFAULT 'active',
    started_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ends_at       TIMESTAMPTZ,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- NEGOCIO — Sucursales
-- ============================================================

CREATE TABLE branches (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id  UUID NOT NULL REFERENCES saas_tenants(id),
    name       TEXT NOT NULL,
    address    TEXT,
    is_active  BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

-- ============================================================
-- NEGOCIO — Usuarios
-- ============================================================

CREATE TABLE users (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id     UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id     UUID REFERENCES branches(id),
    phone         TEXT NOT NULL,
    password_hash TEXT NOT NULL,
    role          TEXT NOT NULL,
    name          TEXT NOT NULL,
    is_active     BOOLEAN NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at    TIMESTAMPTZ,
    UNIQUE(tenant_id, phone)
);

-- ============================================================
-- SEED — Plan free/local
-- ============================================================

INSERT INTO saas_plans (id, name, price_usd, max_branches, max_cashiers, features)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'local',
    0.00,
    1,
    1,
    '{"sync": false, "multiBranch": false, "reports": "basic"}'
);
