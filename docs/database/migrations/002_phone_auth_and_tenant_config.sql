-- ============================================================
-- 002_phone_auth_and_tenant_config.sql
-- Replace email with phone as primary identifier.
-- Add currency, business_type, features_config to saas_tenants.
-- ============================================================

-- saas_tenants: drop email (NOT NULL UNIQUE), add phone + config fields
ALTER TABLE saas_tenants
    DROP COLUMN email;

ALTER TABLE saas_tenants
    ADD COLUMN phone        TEXT NOT NULL,
    ADD COLUMN currency     TEXT NOT NULL DEFAULT 'BOB',      -- 'BOB' | 'USD'
    ADD COLUMN business_type TEXT NOT NULL DEFAULT 'other',   -- 'restaurant' | 'pizzeria' | 'bakery' | 'retail' | 'other'
    ADD COLUMN features_config JSONB NOT NULL DEFAULT '{}';   -- feature flags from onboarding

ALTER TABLE saas_tenants
    ADD CONSTRAINT saas_tenants_phone_unique UNIQUE (phone);

-- users: replace email with phone
ALTER TABLE users
    DROP CONSTRAINT users_tenant_id_email_key,
    DROP COLUMN email;

ALTER TABLE users
    ADD COLUMN phone TEXT NOT NULL DEFAULT '';

ALTER TABLE users
    ADD CONSTRAINT users_tenant_id_phone_key UNIQUE (tenant_id, phone);

-- Seed: plan free/local (insert so backend can look it up at register time)
INSERT INTO saas_plans (id, name, price_usd, max_branches, max_cashiers, features)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'local',
    0.00,
    1,
    1,
    '{"sync": false, "multiBranch": false, "reports": "basic"}'
)
ON CONFLICT DO NOTHING;
