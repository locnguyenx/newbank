-- V2__create_product_schema.sql
-- Product schema for Corporate & SME Banking System

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    family VARCHAR(30) NOT NULL,
    description TEXT,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE TABLE product_versions (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    version_number INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    submitted_by VARCHAR(255),
    approved_by VARCHAR(255),
    rejection_comment TEXT,
    contract_count BIGINT DEFAULT 0,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    UNIQUE (product_id, version_number)
);

CREATE INDEX idx_product_versions_product_id ON product_versions(product_id);
CREATE INDEX idx_product_versions_status ON product_versions(status);

CREATE TABLE product_features (
    id BIGSERIAL PRIMARY KEY,
    product_version_id BIGINT NOT NULL REFERENCES product_versions(id),
    feature_key VARCHAR(100) NOT NULL,
    feature_value VARCHAR(500) NOT NULL,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    UNIQUE (product_version_id, feature_key)
);

CREATE TABLE product_fee_entries (
    id BIGSERIAL PRIMARY KEY,
    product_version_id BIGINT NOT NULL REFERENCES product_versions(id),
    fee_type VARCHAR(100) NOT NULL,
    calculation_method VARCHAR(30) NOT NULL,
    amount NUMERIC(19,4),
    rate NUMERIC(10,6),
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE TABLE product_fee_tiers (
    id BIGSERIAL PRIMARY KEY,
    fee_entry_id BIGINT NOT NULL REFERENCES product_fee_entries(id) ON DELETE CASCADE,
    tier_from BIGINT NOT NULL,
    tier_to BIGINT,
    rate NUMERIC(19,6) NOT NULL
);

CREATE INDEX idx_product_fee_tiers_fee_entry_id ON product_fee_tiers(fee_entry_id);

CREATE TABLE product_customer_segments (
    id BIGSERIAL PRIMARY KEY,
    product_version_id BIGINT NOT NULL REFERENCES product_versions(id),
    customer_type VARCHAR(30) NOT NULL,
    UNIQUE (product_version_id, customer_type)
);

CREATE TABLE product_audit_logs (
    id BIGSERIAL PRIMARY KEY,
    product_version_id BIGINT NOT NULL REFERENCES product_versions(id),
    action VARCHAR(30) NOT NULL,
    actor VARCHAR(100) NOT NULL,
    from_status VARCHAR(30),
    to_status VARCHAR(30),
    comment TEXT,
    maker_username VARCHAR(100),
    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX idx_product_audit_logs_product_version_id ON product_audit_logs(product_version_id);
