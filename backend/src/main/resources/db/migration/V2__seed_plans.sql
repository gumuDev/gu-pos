-- ============================================================
-- V2__seed_plans.sql
-- Insert plans: basic and pro. Update local plan features.
-- Add ends_at to saas_subscriptions.
-- ============================================================

ALTER TABLE saas_subscriptions
    ADD COLUMN IF NOT EXISTS ends_at TIMESTAMPTZ;

-- Update local plan with full features definition
UPDATE saas_plans
SET features = '{
    "sync": false,
    "advancedReports": false,
    "maxCashiers": 2,
    "maxBranches": 1,
    "multiBranch": false,
    "telegramStockAlerts": false,
    "csvExport": false
}'
WHERE id = '00000000-0000-0000-0000-000000000001';

-- Plan: basic (20 BOB/month)
INSERT INTO saas_plans (id, name, price_usd, max_branches, max_cashiers, features)
VALUES (
    '00000000-0000-0000-0000-000000000002',
    'basic',
    20.00,
    1,
    2,
    '{
        "sync": true,
        "advancedReports": true,
        "maxCashiers": 2,
        "maxBranches": 1,
        "multiBranch": false,
        "telegramStockAlerts": true,
        "csvExport": true
    }'
)
ON CONFLICT DO NOTHING;

-- Plan: pro (50 BOB/month)
INSERT INTO saas_plans (id, name, price_usd, max_branches, max_cashiers, features)
VALUES (
    '00000000-0000-0000-0000-000000000003',
    'pro',
    50.00,
    NULL,
    5,
    '{
        "sync": true,
        "advancedReports": true,
        "maxCashiers": 5,
        "maxBranches": null,
        "multiBranch": true,
        "telegramStockAlerts": true,
        "csvExport": true
    }'
)
ON CONFLICT DO NOTHING;
