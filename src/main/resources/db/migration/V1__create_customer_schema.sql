-- V1__create_customer_schema.sql
-- Initial customer schema for Corporate & SME Banking System

-- Customer base table (joined inheritance with discriminator)
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    customer_number VARCHAR(50) NOT NULL UNIQUE,
    customer_type VARCHAR(31) NOT NULL,
    status VARCHAR(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    tax_id VARCHAR(50),
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

-- Corporate customer extension
CREATE TABLE corporate_customers (
    id BIGINT PRIMARY KEY REFERENCES customers(id),
    registration_number VARCHAR(100),
    industry VARCHAR(255),
    annual_revenue_amount NUMERIC(19,2),
    annual_revenue_currency VARCHAR(3),
    employee_count INTEGER,
    website VARCHAR(500)
);

-- SME customer extension
CREATE TABLE sme_customers (
    id BIGINT PRIMARY KEY REFERENCES customers(id),
    registration_number VARCHAR(100),
    industry VARCHAR(255),
    business_type VARCHAR(255),
    annual_turnover_amount NUMERIC(19,2),
    annual_turnover_currency VARCHAR(3),
    years_in_operation INTEGER
);

-- Individual customer extension
CREATE TABLE individual_customers (
    id BIGINT PRIMARY KEY REFERENCES customers(id),
    date_of_birth DATE,
    place_of_birth VARCHAR(255),
    nationality VARCHAR(100),
    employer_id BIGINT,
    employment_status VARCHAR(50)
);

-- Customer address collection
CREATE TABLE customer_addresses (
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    street VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    postal_code VARCHAR(20),
    country VARCHAR(100)
);

-- Customer phone collection
CREATE TABLE customer_phones (
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    country_code VARCHAR(10),
    phone_number VARCHAR(50),
    phone_type VARCHAR(20)
);

-- Customer email collection
CREATE TABLE customer_emails (
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    email VARCHAR(255)
);

-- Employment relationship
CREATE TABLE employment_relationships (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES customers(id),
    employer_id BIGINT NOT NULL REFERENCES customers(id),
    employee_number VARCHAR(50),
    department VARCHAR(255),
    position VARCHAR(255),
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

-- Customer authorization
CREATE TABLE customer_authorizations (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    authorized_person_id BIGINT NOT NULL REFERENCES customers(id),
    relationship_type VARCHAR(50) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    effective_date DATE NOT NULL,
    expiration_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- Authorization document
CREATE TABLE authorization_documents (
    id BIGSERIAL PRIMARY KEY,
    authorization_id BIGINT NOT NULL REFERENCES customer_authorizations(id),
    document_type VARCHAR(50) NOT NULL,
    document_reference VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL
);

-- KYC check
CREATE TABLE kyc_checks (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    kyc_level VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    assigned_officer VARCHAR(255),
    risk_score INTEGER,
    due_date TIMESTAMP,
    completed_at TIMESTAMP,
    next_review_date TIMESTAMP,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

-- KYC document
CREATE TABLE kyc_documents (
    id BIGSERIAL PRIMARY KEY,
    kyc_check_id BIGINT NOT NULL REFERENCES kyc_checks(id),
    document_type VARCHAR(50) NOT NULL,
    document_reference VARCHAR(500) NOT NULL,
    verification_status VARCHAR(20) NOT NULL,
    verified_by VARCHAR(255),
    verified_at TIMESTAMP,
    expiry_date TIMESTAMP
);

-- Sanctions screening result
CREATE TABLE sanctions_screening_results (
    id BIGSERIAL PRIMARY KEY,
    kyc_check_id BIGINT NOT NULL REFERENCES kyc_checks(id),
    screened_at TIMESTAMP NOT NULL,
    result VARCHAR(20) NOT NULL,
    matched_names TEXT,
    is_cleared BOOLEAN NOT NULL DEFAULT FALSE,
    cleared_by VARCHAR(255),
    cleared_at TIMESTAMP
);

-- Bulk upload record
CREATE TABLE bulk_upload_records (
    id BIGSERIAL PRIMARY KEY,
    employer_id BIGINT NOT NULL REFERENCES customers(id),
    uploaded_by VARCHAR(255) NOT NULL,
    total_records INTEGER NOT NULL,
    processed_records INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);
