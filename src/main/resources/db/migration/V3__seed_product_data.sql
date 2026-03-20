-- V3__seed_product_data.sql
-- Seed data for product catalog

INSERT INTO products (code, name, family, description, created_by) VALUES
('CURRENT', 'Current Account', 'ACCOUNT', 'Standard current account with overdraft facility', 'system'),
('SAVINGS', 'Savings Account', 'ACCOUNT', 'Interest-bearing savings account', 'system'),
('FIXED-DEPOSIT', 'Fixed Deposit', 'ACCOUNT', 'Term deposit with fixed interest rate', 'system'),
('LOAN', 'Loan Account', 'ACCOUNT', 'Business loan account', 'system');

INSERT INTO product_versions (product_id, version_number, status, created_by) VALUES
(1, 1, 'ACTIVE', 'system'),
(2, 1, 'ACTIVE', 'system'),
(3, 1, 'ACTIVE', 'system'),
(4, 1, 'ACTIVE', 'system');
