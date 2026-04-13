CREATE TABLE support_reports (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    type            TEXT NOT NULL CHECK (type IN ('bug', 'suggestion')),
    description     TEXT NOT NULL,
    screenshot_url  TEXT,
    status          TEXT NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'reviewed')),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_support_reports_tenant ON support_reports(tenant_id);
CREATE INDEX idx_support_reports_status ON support_reports(status);
