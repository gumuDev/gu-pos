-- ============================================================
-- Fase 1: Separar estado actual de historial de suscripciones
-- ============================================================

-- 1. Agregar updated_at a saas_subscriptions
ALTER TABLE saas_subscriptions
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW();

-- 2. Crear tabla de historial
CREATE TABLE saas_subscription_history (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id    UUID NOT NULL REFERENCES saas_tenants(id),
    plan_id      UUID NOT NULL REFERENCES saas_plans(id),
    status       TEXT NOT NULL DEFAULT 'active',
    started_at   TIMESTAMPTZ NOT NULL,
    ends_at      TIMESTAMPTZ,
    activated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_subscription_history_tenant ON saas_subscription_history(tenant_id);

-- 3. Copiar registros existentes al historial
INSERT INTO saas_subscription_history (id, tenant_id, plan_id, status, started_at, ends_at, activated_at, created_at)
SELECT id, tenant_id, plan_id, status, started_at, ends_at, created_at, created_at
FROM saas_subscriptions;

-- 4. Deduplicar saas_subscriptions — dejar solo la fila más reciente por tenant
DELETE FROM saas_subscriptions
WHERE id NOT IN (
    SELECT DISTINCT ON (tenant_id) id
    FROM saas_subscriptions
    ORDER BY tenant_id, created_at DESC
);
