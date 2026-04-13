-- ============================================================
-- 001_initial_schema.sql
-- Schema inicial del sistema POS SaaS
-- ============================================================

-- ============================================================
-- SAAS — Nivel plataforma
-- ============================================================

CREATE TABLE saas_plans (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        TEXT NOT NULL,                          -- 'local' | 'cloud' | 'business'
    price_usd   NUMERIC(10,2) NOT NULL DEFAULT 0,
    max_branches INT,                                   -- NULL = ilimitado
    max_cashiers INT,                                   -- NULL = ilimitado
    features    JSONB NOT NULL DEFAULT '{}',
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE saas_tenants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            TEXT NOT NULL,
    email           TEXT NOT NULL UNIQUE,
    plan_id         UUID NOT NULL REFERENCES saas_plans(id),
    status          TEXT NOT NULL DEFAULT 'active',     -- 'active' | 'suspended' | 'cancelled'
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE saas_subscriptions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    plan_id         UUID NOT NULL REFERENCES saas_plans(id),
    stripe_sub_id   TEXT,
    status          TEXT NOT NULL DEFAULT 'active' CONSTRAINT chk_subscription_status CHECK (status IN ('active', 'expired', 'canceled', 'suspended', 'past_due')),
    started_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ends_at         TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE saas_telemetry (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id       UUID NOT NULL,
    business_name   TEXT NOT NULL,
    app_version     TEXT NOT NULL,
    plan            TEXT NOT NULL DEFAULT 'local',
    last_active_at  TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- NEGOCIO — Sucursales y bodegas
-- ============================================================

CREATE TABLE branches (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    name        TEXT NOT NULL,
    address     TEXT,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at  TIMESTAMPTZ
);

CREATE TABLE warehouses (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    name        TEXT NOT NULL,
    address     TEXT,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at  TIMESTAMPTZ
);

-- ============================================================
-- NEGOCIO — Catálogo de productos
-- ============================================================

CREATE TABLE categories (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    name        TEXT NOT NULL,
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
    description     TEXT,                               -- visible al cliente
    image_url       TEXT,                               -- Supabase Storage
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    sync_status     TEXT NOT NULL DEFAULT 'synced',     -- 'synced' | 'pending' | 'conflict'
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE TABLE product_variants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    product_id      UUID NOT NULL REFERENCES products(id),
    name            TEXT NOT NULL,                      -- 'Personal' | 'Mediana' | 'Única', etc.
    base_price      NUMERIC(10,2) NOT NULL,
    sku             TEXT,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    sync_status     TEXT NOT NULL DEFAULT 'synced',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE TABLE branch_prices (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id   UUID NOT NULL REFERENCES branches(id),
    variant_id  UUID NOT NULL REFERENCES product_variants(id),
    price       NUMERIC(10,2) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(branch_id, variant_id)
);

-- ============================================================
-- NEGOCIO — Inventario y recetas
-- ============================================================

CREATE TABLE ingredients (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES saas_tenants(id),
    name        TEXT NOT NULL,
    unit        TEXT NOT NULL,                          -- 'g' | 'ml' | 'unidad' | 'kg' | 'l'
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at  TIMESTAMPTZ
);

CREATE TABLE recipes (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    variant_id      UUID NOT NULL REFERENCES product_variants(id),
    ingredient_id   UUID NOT NULL REFERENCES ingredients(id),
    quantity        NUMERIC(10,3) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(variant_id, ingredient_id)
);

CREATE TABLE branch_stock (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id       UUID NOT NULL REFERENCES branches(id),
    ingredient_id   UUID NOT NULL REFERENCES ingredients(id),
    quantity        NUMERIC(10,3) NOT NULL DEFAULT 0,
    min_quantity    NUMERIC(10,3) NOT NULL DEFAULT 0,   -- umbral alerta stock bajo
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(branch_id, ingredient_id)
);

CREATE TABLE warehouse_stock (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    warehouse_id    UUID NOT NULL REFERENCES warehouses(id),
    ingredient_id   UUID NOT NULL REFERENCES ingredients(id),
    quantity        NUMERIC(10,3) NOT NULL DEFAULT 0,
    min_quantity    NUMERIC(10,3) NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(warehouse_id, ingredient_id)
);

CREATE TABLE stock_movements (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id       UUID REFERENCES branches(id),
    warehouse_id    UUID REFERENCES warehouses(id),
    ingredient_id   UUID NOT NULL REFERENCES ingredients(id),
    quantity        NUMERIC(10,3) NOT NULL,             -- positivo = entrada, negativo = salida
    type            TEXT NOT NULL,                      -- 'purchase' | 'sale' | 'adjustment' | 'transfer'
    reference_id    UUID,                               -- order_id si type = 'sale'
    notes           TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- NEGOCIO — Promociones
-- ============================================================

CREATE TABLE promotions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id       UUID REFERENCES branches(id),       -- NULL = aplica a todas las sucursales
    name            TEXT NOT NULL,
    type            TEXT NOT NULL,                      -- 'BUY_X_GET_Y' | 'PERCENTAGE' | 'COMBO'
    days_of_week    INT[] NOT NULL DEFAULT '{0,1,2,3,4,5,6}',  -- 0=domingo
    start_date      DATE NOT NULL,
    end_date        DATE,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE TABLE promotion_rules (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL REFERENCES saas_tenants(id),
    promotion_id        UUID NOT NULL REFERENCES promotions(id),
    variant_id          UUID REFERENCES product_variants(id),
    buy_quantity        INT,                            -- para BUY_X_GET_Y
    get_quantity        INT,                            -- para BUY_X_GET_Y
    discount_percent    NUMERIC(5,2),                  -- para PERCENTAGE
    combo_price         NUMERIC(10,2),                 -- para COMBO
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- NEGOCIO — Usuarios y sesiones
-- ============================================================

CREATE TABLE users (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id       UUID REFERENCES branches(id),       -- NULL = acceso a todas (admin)
    email           TEXT NOT NULL,
    password_hash   TEXT NOT NULL,
    role            TEXT NOT NULL,                      -- 'business_admin' | 'cashier'
    name            TEXT NOT NULL,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ,
    UNIQUE(tenant_id, email)
);

CREATE TABLE cashier_sessions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id       UUID NOT NULL REFERENCES branches(id),
    cashier_id      UUID NOT NULL REFERENCES users(id),
    opening_amount  NUMERIC(10,2) NOT NULL DEFAULT 0,
    closing_amount  NUMERIC(10,2),
    opened_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    closed_at       TIMESTAMPTZ,
    notes           TEXT
);

-- ============================================================
-- NEGOCIO — Ventas
-- ============================================================

CREATE TABLE orders (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES saas_tenants(id),
    branch_id       UUID NOT NULL REFERENCES branches(id),
    cashier_id      UUID NOT NULL REFERENCES users(id),
    session_id      UUID REFERENCES cashier_sessions(id),
    subtotal        NUMERIC(10,2) NOT NULL,
    discount_total  NUMERIC(10,2) NOT NULL DEFAULT 0,
    total           NUMERIC(10,2) NOT NULL,
    payment_method  TEXT NOT NULL,                      -- 'cash' | 'card' | 'mixed'
    status          TEXT NOT NULL DEFAULT 'completed',  -- 'completed' | 'cancelled' | 'refunded'
    sync_status     TEXT NOT NULL DEFAULT 'synced',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE order_items (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL REFERENCES saas_tenants(id),
    order_id            UUID NOT NULL REFERENCES orders(id),
    variant_id          UUID NOT NULL REFERENCES product_variants(id),
    quantity            INT NOT NULL,
    unit_price          NUMERIC(10,2) NOT NULL,
    discount_applied    NUMERIC(10,2) NOT NULL DEFAULT 0,
    subtotal            NUMERIC(10,2) NOT NULL,
    promotion_id        UUID REFERENCES promotions(id),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- ÍNDICES
-- ============================================================

CREATE INDEX idx_products_tenant ON products(tenant_id);
CREATE INDEX idx_orders_tenant_branch ON orders(tenant_id, branch_id);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_stock_movements_tenant ON stock_movements(tenant_id, created_at);
CREATE INDEX idx_branch_stock_branch ON branch_stock(branch_id);

-- ============================================================
-- RLS (Row Level Security)
-- ============================================================

ALTER TABLE branches ENABLE ROW LEVEL SECURITY;
ALTER TABLE products ENABLE ROW LEVEL SECURITY;
ALTER TABLE product_variants ENABLE ROW LEVEL SECURITY;
ALTER TABLE orders ENABLE ROW LEVEL SECURITY;
ALTER TABLE order_items ENABLE ROW LEVEL SECURITY;
ALTER TABLE branch_stock ENABLE ROW LEVEL SECURITY;
ALTER TABLE stock_movements ENABLE ROW LEVEL SECURITY;

-- El backend controla el acceso via tenant_id en JWT
-- RLS como segunda capa de seguridad
