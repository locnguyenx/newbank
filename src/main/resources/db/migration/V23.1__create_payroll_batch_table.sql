-- V23__create_payroll_batch_table.sql
CREATE TABLE payroll_batch (
    id BIGSERIAL PRIMARY KEY,
    batch_reference VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    file_format VARCHAR(20) NOT NULL,
    record_count INTEGER,
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

CREATE INDEX idx_payroll_batch_customer_id ON payroll_batch(customer_id);
CREATE INDEX idx_payroll_batch_status ON payroll_batch(status);
