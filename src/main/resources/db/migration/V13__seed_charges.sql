-- V13__seed_charges.sql
-- Seed sample charge definitions

INSERT INTO charge_definitions (name, charge_type, currency, status, created_at, created_by, updated_at, updated_by)
VALUES 
  ('Monthly Maintenance Fee', 'MONTHLY_MAINTENANCE', 'USD', 'ACTIVE', NOW(), 'system', NOW(), 'system'),
  ('Wire Transfer Fee', 'WIRE_TRANSFER_FEE', 'USD', 'ACTIVE', NOW(), 'system', NOW(), 'system'),
  ('Overdraft Fee', 'OVERDRAFT_FEE', 'USD', 'ACTIVE', NOW(), 'system', NOW(), 'system');

-- Seed charge rules for Monthly Maintenance Fee
INSERT INTO charge_rules (charge_definition_id, calculation_method, flat_amount, created_at, created_by, updated_at, updated_by)
VALUES (1, 'FLAT', 25.0000, NOW(), 'system', NOW(), 'system');

-- Seed charge rules for Wire Transfer Fee
INSERT INTO charge_rules (charge_definition_id, calculation_method, percentage_rate, min_amount, max_amount, created_at, created_by, updated_at, updated_by)
VALUES (2, 'PERCENTAGE', 0.001000, 5.0000, 100.0000, NOW(), 'system', NOW(), 'system');