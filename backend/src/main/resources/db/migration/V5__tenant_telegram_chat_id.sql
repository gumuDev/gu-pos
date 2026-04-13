ALTER TABLE saas_tenants
    ADD COLUMN IF NOT EXISTS telegram_chat_id BIGINT;
