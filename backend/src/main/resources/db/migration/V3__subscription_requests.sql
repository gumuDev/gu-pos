-- ============================================================
-- V3__subscription_requests.sql
-- Table to track subscription payment requests from clients.
-- ============================================================

CREATE TABLE subscription_requests (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    plan_name       TEXT NOT NULL,
    transaction_ref TEXT,
    receipt_url     TEXT,
    status          TEXT NOT NULL DEFAULT 'pending', -- 'pending' | 'approved' | 'rejected'
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_subscription_requests_tenant ON subscription_requests(tenant_id, status);
