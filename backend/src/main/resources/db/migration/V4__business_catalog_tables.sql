-- ============================================================
-- V4__business_catalog_tables.sql
-- Business catalog tables: categories, products, variants,
-- modifiers, combos, orders, order_items, stock.
-- ============================================================

CREATE TABLE categories (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    name        TEXT NOT NULL,
    color       TEXT,
    sort_order  INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at  TIMESTAMPTZ
);

CREATE TABLE products (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    category_id     UUID REFERENCES categories(id),
    name            TEXT NOT NULL,
    image_emoji     TEXT,
    base_price      NUMERIC(10,2) NOT NULL DEFAULT 0,
    stock           INT NOT NULL DEFAULT -1,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    sync_status     TEXT NOT NULL DEFAULT 'synced',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE TABLE product_variants (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    product_id  UUID NOT NULL REFERENCES products(id),
    name        TEXT NOT NULL,
    price       NUMERIC(10,2) NOT NULL,
    stock       INT NOT NULL DEFAULT -1,
    sort_order  INT NOT NULL DEFAULT 0,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at  TIMESTAMPTZ
);

CREATE TABLE modifier_groups (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    product_id  UUID NOT NULL REFERENCES products(id),
    name        TEXT NOT NULL,
    required    BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order  INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE modifier_options (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    group_id    UUID NOT NULL REFERENCES modifier_groups(id),
    name        TEXT NOT NULL,
    price_delta NUMERIC(10,2) NOT NULL DEFAULT 0,
    sort_order  INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE combos (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    name        TEXT NOT NULL,
    description TEXT,
    price       NUMERIC(10,2) NOT NULL,
    image_emoji TEXT,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at  TIMESTAMPTZ
);

CREATE TABLE combo_items (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id    UUID NOT NULL REFERENCES saas_tenants(id),
    combo_id     UUID NOT NULL REFERENCES combos(id),
    product_id   UUID NOT NULL REFERENCES products(id),
    product_name TEXT NOT NULL,
    quantity     INT NOT NULL DEFAULT 1,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE cashier_sessions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id       UUID NOT NULL REFERENCES branches(id),
    cashier_name    TEXT NOT NULL,
    opening_amount  NUMERIC(10,2) NOT NULL DEFAULT 0,
    closing_amount  NUMERIC(10,2),
    status          TEXT NOT NULL DEFAULT 'open',
    opened_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    closed_at       TIMESTAMPTZ
);

CREATE TABLE orders (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id       UUID NOT NULL REFERENCES branches(id),
    session_id      UUID REFERENCES cashier_sessions(id),
    cashier_name    TEXT NOT NULL,
    subtotal        NUMERIC(10,2) NOT NULL,
    discount_total  NUMERIC(10,2) NOT NULL DEFAULT 0,
    total           NUMERIC(10,2) NOT NULL,
    payment_method  TEXT NOT NULL,
    cash_tendered   NUMERIC(10,2),
    change_amount   NUMERIC(10,2),
    daily_sequence  INT NOT NULL DEFAULT 0,
    status          TEXT NOT NULL DEFAULT 'completed',
    sync_status     TEXT NOT NULL DEFAULT 'synced',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE order_items (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    order_id        UUID NOT NULL REFERENCES orders(id),
    product_id      UUID NOT NULL REFERENCES products(id),
    product_name    TEXT NOT NULL,
    variant_id      UUID REFERENCES product_variants(id),
    variant_name    TEXT,
    modifiers_json  TEXT,
    unit_price      NUMERIC(10,2) NOT NULL,
    quantity        INT NOT NULL,
    subtotal        NUMERIC(10,2) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indices
CREATE INDEX idx_products_tenant ON products(tenant_id);
CREATE INDEX idx_orders_tenant ON orders(tenant_id, created_at);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_categories_tenant ON categories(tenant_id);
