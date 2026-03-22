-- V17__seed_customer_data.sql
-- Seed data for customers

-- Corporate Customers
INSERT INTO customers (customer_number, customer_type, status, name, tax_id, created_at, created_by, updated_at, updated_by)
VALUES
    ('CUST-001', 'CORPORATE', 'ACTIVE', 'Acme Corporation', 'REG-123456', NOW(), 'system', NOW(), 'system'),
    ('CUST-005', 'CORPORATE', 'ACTIVE', 'Global Industries Inc', 'REG-567890', NOW(), 'system', NOW(), 'system');

INSERT INTO corporate_customers (id, registration_number, industry, annual_revenue_amount, annual_revenue_currency, employee_count)
VALUES
    (1, 'REG-123456', 'Technology', 50000000.00, 'USD', 250),
    (2, 'REG-567890', 'Manufacturing', 100000000.00, 'USD', 500);

-- SME Customers
INSERT INTO customers (customer_number, customer_type, status, name, tax_id, created_at, created_by, updated_at, updated_by)
VALUES
    ('CUST-002', 'SME', 'ACTIVE', 'Tech Solutions Ltd', 'SME-789012', NOW(), 'system', NOW(), 'system');

INSERT INTO sme_customers (id, registration_number, industry, business_type, annual_turnover_amount, annual_turnover_currency, years_in_operation)
VALUES
    (3, 'SME-789012', 'Software', 'OTHER', 1500000.00, 'USD', 5);

-- Individual Customers
INSERT INTO customers (customer_number, customer_type, status, name, tax_id, created_at, created_by, updated_at, updated_by)
VALUES
    ('CUST-003', 'INDIVIDUAL', 'ACTIVE', 'John Doe', 'ID-345678', NOW(), 'system', NOW(), 'system'),
    ('CUST-004', 'INDIVIDUAL', 'PENDING', 'Jane Smith', 'ID-901234', NOW(), 'system', NOW(), 'system');

INSERT INTO individual_customers (id, date_of_birth, nationality)
VALUES
    (4, '1990-01-15', 'US'),
    (5, '1985-06-20', 'US');

-- Customer Emails
INSERT INTO customer_emails (customer_id, email)
VALUES
    (1, 'contact@acme.com'),
    (2, 'info@globalind.com'),
    (3, 'info@techsolutions.com'),
    (4, 'john.doe@email.com'),
    (5, 'jane.smith@email.com');

-- Customer Phones
INSERT INTO customer_phones (customer_id, country_code, phone_number, phone_type)
VALUES
    (1, '+1', '555-0100', 'MOBILE'),
    (2, '+1', '555-0500', 'MOBILE'),
    (3, '+1', '555-0200', 'MOBILE'),
    (4, '+1', '555-0300', 'MOBILE'),
    (5, '+1', '555-0400', 'MOBILE');
