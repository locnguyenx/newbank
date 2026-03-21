-- V8__create_master_data_tables.sql
-- Create remaining master data tables (industries, branches, channels, document_types) and indexes

CREATE TABLE industries (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    parent_code VARCHAR(10),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE TABLE branches (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country_code VARCHAR(2) REFERENCES countries(iso_code),
    address TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE TABLE channels (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE TABLE document_types (
    code VARCHAR(30) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(30) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE INDEX idx_exchange_rates_base_target ON exchange_rates (base_currency, target_currency);
CREATE INDEX idx_holidays_country_date ON holidays (country_code, holiday_date);
CREATE INDEX idx_document_types_category ON document_types (category);
