-- V11__seed_limits.sql
-- Seed sample limit definitions

INSERT INTO limit_definitions (name, limit_type, amount, currency, status, created_at, created_by, updated_at, updated_by)
VALUES ('Daily Transfer Limit', 'DAILY', 50000.0000, 'USD', 'ACTIVE', NOW(), 'system', NOW(), 'system');

INSERT INTO limit_definitions (name, limit_type, amount, currency, status, created_at, created_by, updated_at, updated_by)
VALUES ('Monthly Transfer Limit', 'MONTHLY', 500000.0000, 'USD', 'ACTIVE', NOW(), 'system', NOW(), 'system');
