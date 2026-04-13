-- Migration 003: Add CHECK constraint to saas_subscriptions.status
-- Apply manually in Supabase SQL Editor

ALTER TABLE saas_subscriptions
    ADD CONSTRAINT chk_subscription_status
    CHECK (status IN ('active', 'expired', 'canceled', 'suspended', 'past_due'));
