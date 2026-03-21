-- V5__add_audit_columns_to_product_customer_segments.sql
-- Add missing audit columns to product_customer_segments table

ALTER TABLE product_customer_segments ADD COLUMN created_at TIMESTAMP;
ALTER TABLE product_customer_segments ADD COLUMN created_by VARCHAR(255);
ALTER TABLE product_customer_segments ADD COLUMN updated_at TIMESTAMP;
ALTER TABLE product_customer_segments ADD COLUMN updated_by VARCHAR(255);
