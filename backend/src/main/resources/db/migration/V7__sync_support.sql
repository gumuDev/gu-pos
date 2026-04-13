-- ============================================================
-- V7__sync_support.sql
-- Add deleted_at to tables missing it (required for WatermelonDB sync soft-delete).
-- Add updated_at_ms (bigint epoch ms) for efficient pull queries.
-- ============================================================

-- Add deleted_at to tables that are missing it
ALTER TABLE modifier_groups  ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ;
ALTER TABLE modifier_options ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ;
ALTER TABLE combo_items      ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ;
ALTER TABLE order_items      ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ;

-- Add updated_at to tables that only have created_at
ALTER TABLE combo_items  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW();
ALTER TABLE order_items  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW();

-- Indices for efficient pull queries (filter by tenant + updated_at range)
CREATE INDEX IF NOT EXISTS idx_categories_sync       ON categories(tenant_id, updated_at);
CREATE INDEX IF NOT EXISTS idx_products_sync         ON products(tenant_id, updated_at);
CREATE INDEX IF NOT EXISTS idx_product_variants_sync ON product_variants(tenant_id, updated_at);
CREATE INDEX IF NOT EXISTS idx_modifier_groups_sync  ON modifier_groups(tenant_id, updated_at);
CREATE INDEX IF NOT EXISTS idx_modifier_options_sync ON modifier_options(tenant_id, updated_at);
CREATE INDEX IF NOT EXISTS idx_combos_sync           ON combos(tenant_id, updated_at);
CREATE INDEX IF NOT EXISTS idx_combo_items_sync      ON combo_items(tenant_id, updated_at);
CREATE INDEX IF NOT EXISTS idx_orders_sync           ON orders(tenant_id, updated_at);
CREATE INDEX IF NOT EXISTS idx_order_items_sync      ON order_items(tenant_id, updated_at);
