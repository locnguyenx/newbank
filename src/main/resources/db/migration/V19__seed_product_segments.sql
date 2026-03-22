-- V19__seed_product_segments.sql
-- Assign customer segments to existing products

INSERT INTO product_customer_segments (product_version_id, customer_type) VALUES
(1, 'CORPORATE'),
(1, 'SME'),
(1, 'INDIVIDUAL'),
(2, 'CORPORATE'),
(2, 'SME'),
(2, 'INDIVIDUAL'),
(3, 'CORPORATE'),
(3, 'SME'),
(4, 'CORPORATE'),
(4, 'SME');
