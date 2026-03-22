-- V18__seed_account_data.sql
-- Seed data for accounts

-- Insert accounts for each customer type
INSERT INTO accounts (account_number, account_type, status, currency, balance, customer_id, product_id, product_version_id, product_name, opened_at, created_at, created_by, updated_at, updated_by)
VALUES
    ('ACC-001-001', 'CURRENT', 'ACTIVE', 'USD', 150000.00, 1, 1, 1, 'Standard Current Account', NOW(), NOW(), 'system', NOW(), 'system'),
    ('ACC-001-002', 'SAVINGS', 'ACTIVE', 'USD', 75000.00, 1, 2, 2, 'Premium Savings Account', NOW(), NOW(), 'system', NOW(), 'system'),
    ('ACC-002-001', 'FIXED_DEPOSIT', 'ACTIVE', 'GBP', 25000.00, 2, 3, 3, 'Fixed Deposit', NOW(), NOW(), 'system', NOW(), 'system'),
    ('ACC-003-001', 'CURRENT', 'ACTIVE', 'EUR', 50000.00, 3, 1, 1, 'Standard Current Account', NOW(), NOW(), 'system', NOW(), 'system'),
    ('ACC-004-001', 'SAVINGS', 'ACTIVE', 'USD', 10000.00, 4, 2, 2, 'Premium Savings Account', NOW(), NOW(), 'system', NOW(), 'system'),
    ('ACC-005-001', 'LOAN', 'ACTIVE', 'USD', 500000.00, 2, 4, 4, 'Loan Account', NOW(), NOW(), 'system', NOW(), 'system');

-- Account-specific tables
INSERT INTO current_account (id, overdraft_limit, interest_rate)
VALUES (1, 50000.00, 12.50);

INSERT INTO savings_account (id, minimum_balance, interest_rate, last_interest_posted)
VALUES
    (2, 1000.00, 3.25, '2024-01-01'),
    (4, 500.00, 2.75, '2024-01-01');

INSERT INTO fixed_deposit_account (id, deposit_term, maturity_date, maturity_amount)
VALUES (3, 365, '2025-01-01', 26250.00);

INSERT INTO loan_account (id, loan_amount, interest_rate, term, outstanding_balance, next_payment_date)
VALUES (5, 500000.00, 8.50, 60, 475000.00, '2024-02-01');
