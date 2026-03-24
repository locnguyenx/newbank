-- V24__create_cash_management_tables.sql

-- Payroll Record
CREATE TABLE payroll_record (
    id BIGSERIAL PRIMARY KEY,
    payroll_batch_id BIGINT NOT NULL,
    employee_identifier VARCHAR(100),
    employee_name VARCHAR(255),
    bank_account_number VARCHAR(50),
    bank_code VARCHAR(50),
    amount DECIMAL(19, 4),
    currency VARCHAR(3),
    status VARCHAR(20) NOT NULL,
    error_code VARCHAR(50),
    error_description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_payroll_record_batch_id ON payroll_record(payroll_batch_id);

-- Receivable Invoice
CREATE TABLE receivable_invoice (
    id BIGSERIAL PRIMARY KEY,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    bill_to_customer_id BIGINT,
    amount DECIMAL(19, 4),
    currency VARCHAR(3),
    issue_date DATE,
    due_date DATE,
    status VARCHAR(20) NOT NULL,
    balance_due DECIMAL(19, 4),
    reference_number VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_receivable_invoice_customer ON receivable_invoice(customer_id);
CREATE INDEX idx_receivable_invoice_status ON receivable_invoice(status);

-- Receivable Payment
CREATE TABLE receivable_payment (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL,
    payment_reference VARCHAR(50) NOT NULL UNIQUE,
    amount DECIMAL(19, 4),
    currency VARCHAR(3),
    payment_date DATE,
    payment_method VARCHAR(20),
    bank_details TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_receivable_payment_invoice ON receivable_payment(invoice_id);

-- Liquidity Position
CREATE TABLE liquidity_position (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    calculation_date_time TIMESTAMP NOT NULL,
    total_position DECIMAL(19, 4),
    currency VARCHAR(3),
    account_breakdown TEXT,
    available_liquidity DECIMAL(19, 4),
    projected_liquidity TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_liquidity_position_customer ON liquidity_position(customer_id);

-- Cash Pooling Transaction
CREATE TABLE cash_pooling_transaction (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    pool_reference VARCHAR(50) NOT NULL UNIQUE,
    transaction_date TIMESTAMP NOT NULL,
    from_account_id BIGINT,
    to_account_id BIGINT,
    amount DECIMAL(19, 4),
    currency VARCHAR(3),
    pooling_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_cash_pooling_customer ON cash_pooling_transaction(customer_id);

-- Batch Payment
CREATE TABLE batch_payment (
    id BIGSERIAL PRIMARY KEY,
    batch_reference VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    file_format VARCHAR(20) NOT NULL,
    instruction_count INTEGER,
    processed_count INTEGER,
    error_count INTEGER,
    total_amount DECIMAL(19, 4),
    currency VARCHAR(3),
    payment_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_batch_payment_customer ON batch_payment(customer_id);
CREATE INDEX idx_batch_payment_status ON batch_payment(status);

-- Batch Payment Instruction
CREATE TABLE batch_payment_instruction (
    id BIGSERIAL PRIMARY KEY,
    batch_payment_id BIGINT NOT NULL,
    instruction_reference VARCHAR(50),
    beneficiary_name VARCHAR(255),
    beneficiary_account VARCHAR(50),
    beneficiary_bank_code VARCHAR(50),
    amount DECIMAL(19, 4),
    currency VARCHAR(3),
    payment_type VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    error_code VARCHAR(50),
    error_description TEXT,
    settlement_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_batch_payment_instruction_batch ON batch_payment_instruction(batch_payment_id);

-- Auto Collection Rule
CREATE TABLE auto_collection_rule (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    rule_name VARCHAR(100) NOT NULL,
    description TEXT,
    trigger_condition VARCHAR(20) NOT NULL,
    collection_amount_type VARCHAR(20) NOT NULL,
    collection_amount_value DECIMAL(19, 4),
    currency VARCHAR(3),
    funding_account_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    pre_notification_days INTEGER,
    retry_attempts INTEGER,
    retry_interval_hours INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_auto_collection_rule_customer ON auto_collection_rule(customer_id);

-- Auto Collection Attempt
CREATE TABLE auto_collection_attempt (
    id BIGSERIAL PRIMARY KEY,
    auto_collection_rule_id BIGINT NOT NULL,
    invoice_id BIGINT,
    attempt_date_time TIMESTAMP NOT NULL,
    collection_amount DECIMAL(19, 4),
    currency VARCHAR(3),
    status VARCHAR(30) NOT NULL,
    error_code VARCHAR(50),
    error_description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_auto_collection_attempt_rule ON auto_collection_attempt(auto_collection_rule_id);

-- Cash Management Audit
CREATE TABLE cash_management_audit (
    id BIGSERIAL PRIMARY KEY,
    event_timestamp TIMESTAMP NOT NULL,
    event_type VARCHAR(30) NOT NULL,
    customer_id BIGINT,
    user_id VARCHAR(100),
    user_type VARCHAR(20),
    entity_type VARCHAR(50),
    entity_id VARCHAR(100),
    action_performed VARCHAR(255),
    details TEXT,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE INDEX idx_cash_management_audit_customer ON cash_management_audit(customer_id);
CREATE INDEX idx_cash_management_audit_event_type ON cash_management_audit(event_type);
CREATE INDEX idx_cash_management_audit_timestamp ON cash_management_audit(event_timestamp);
