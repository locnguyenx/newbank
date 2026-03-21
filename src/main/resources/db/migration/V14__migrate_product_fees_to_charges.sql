-- V14__migrate_product_fees_to_charges.sql
-- Migrate ProductFeeEntry data to the Charges module.
-- The Charges module (V12) provides richer features: min/max caps, customer overrides,
-- waivers, interest rates, and status management. Product-level fee entries are
-- deprecated in favor of the centralized Charges module.
--
-- Strategy: Create one ChargeDefinition per (fee_type, product_code) combination,
-- then link it via ProductCharge. ChargeRules and ChargeTiers are created from
-- the corresponding product_fee_entries and product_fee_tiers.
--
-- This migration runs after V12 (charges schema) and V13 (seed charges).
--
-- ROLLBACK NOTE: This migration is not reversible via a standard Flyway rollback.
-- To rollback, manually: DELETE FROM product_charges; DELETE FROM charge_tiers;
-- DELETE FROM charge_rules; DELETE FROM charge_definitions; (retain seed data).
-- All idempotent inserts (WHERE NOT EXISTS) make this migration safe to re-run.

-- Step 1: Create charge_definitions from unique fee_type + product combinations.
-- H2-compatible: INSERT with WHERE NOT EXISTS (no CTE + ON CONFLICT support).
INSERT INTO charge_definitions (name, charge_type, currency, status, created_at, created_by, updated_at, updated_by)
SELECT
    CONCAT(pfe.fee_type, ' - ', p.code) AS name,
    CASE
        WHEN UPPER(pfe.fee_type) LIKE '%MAINTENANCE%' THEN 'MONTHLY_MAINTENANCE'
        WHEN UPPER(pfe.fee_type) LIKE '%TRANSACTION%' THEN 'TRANSACTION_FEE'
        WHEN UPPER(pfe.fee_type) LIKE '%OVERDRAFT%' THEN 'OVERDRAFT_FEE'
        WHEN UPPER(pfe.fee_type) LIKE '%WIRE%' THEN 'WIRE_TRANSFER_FEE'
        WHEN UPPER(pfe.fee_type) LIKE '%STATEMENT%' THEN 'STATEMENT_FEE'
        WHEN UPPER(pfe.fee_type) LIKE '%CLOSURE%' THEN 'EARLY_CLOSURE_FEE'
        WHEN UPPER(pfe.fee_type) LIKE '%INTEREST%' THEN 'INTEREST'
        WHEN UPPER(pfe.fee_type) LIKE '%PROCESSING%' THEN 'PROCESSING_FEE'
        WHEN UPPER(pfe.fee_type) LIKE '%ANNUAL%%' THEN 'ANNUAL_FEE'
        ELSE 'TRANSACTION_FEE'
    END AS charge_type,
    pfe.currency,
    CASE pv.status
        WHEN 'ACTIVE' THEN 'ACTIVE'
        WHEN 'APPROVED' THEN 'ACTIVE'
        ELSE 'INACTIVE'
    END AS status,
    CURRENT_TIMESTAMP,
    'migration',
    CURRENT_TIMESTAMP,
    'migration'
FROM product_fee_entries pfe
JOIN product_versions pv ON pv.id = pfe.product_version_id
JOIN products p ON p.id = pv.product_id
WHERE NOT EXISTS (
    SELECT 1 FROM charge_definitions cd WHERE cd.name = CONCAT(pfe.fee_type, ' - ', p.code)
);

-- Step 2: Create charge_rules from product_fee_entries.
-- Note: product_fee_entries don't have min/max caps — those default to NULL in charge_rules.
INSERT INTO charge_rules (charge_definition_id, calculation_method, flat_amount, percentage_rate, min_amount, max_amount, created_at, created_by, updated_at, updated_by)
SELECT
    cd.id,
    UPPER(pfe.calculation_method),
    pfe.amount,
    pfe.rate,
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    'migration',
    CURRENT_TIMESTAMP,
    'migration'
FROM product_fee_entries pfe
JOIN product_versions pv ON pv.id = pfe.product_version_id
JOIN products p ON p.id = pv.product_id
JOIN charge_definitions cd ON cd.name = CONCAT(pfe.fee_type, ' - ', p.code)
WHERE NOT EXISTS (
    SELECT 1 FROM charge_rules cr
    WHERE cr.charge_definition_id = cd.id
    AND cr.calculation_method = UPPER(pfe.calculation_method)
    AND (cr.flat_amount = pfe.amount OR (cr.flat_amount IS NULL AND pfe.amount IS NULL))
    AND (cr.percentage_rate = pfe.rate OR (cr.percentage_rate IS NULL AND pfe.rate IS NULL))
);

-- Step 3: Create charge_tiers from product_fee_tiers.
-- Join through the same path to link to the correct charge_rule.
INSERT INTO charge_tiers (charge_rule_id, tier_from, tier_to, rate)
SELECT
    cr.id,
    pft.tier_from,
    pft.tier_to,
    pft.rate
FROM product_fee_tiers pft
JOIN product_fee_entries pfe ON pfe.id = pft.fee_entry_id
JOIN product_versions pv ON pv.id = pfe.product_version_id
JOIN products pr ON pr.id = pv.product_id
JOIN charge_definitions cd ON cd.name = CONCAT(pfe.fee_type, ' - ', pr.code)
JOIN charge_rules cr ON cr.charge_definition_id = cd.id
    AND cr.calculation_method = UPPER(pfe.calculation_method)
    AND (cr.flat_amount = pfe.amount OR (cr.flat_amount IS NULL AND pfe.amount IS NULL))
    AND (cr.percentage_rate = pfe.rate OR (cr.percentage_rate IS NULL AND pfe.rate IS NULL))
WHERE NOT EXISTS (
    SELECT 1 FROM charge_tiers ct
    WHERE ct.charge_rule_id = cr.id
    AND ct.tier_from = pft.tier_from
    AND ct.tier_to IS NOT DISTINCT FROM pft.tier_to
    AND ct.rate = pft.rate
);

-- Step 4: Create product_charges linking charge_definitions to product codes.
-- This allows the Charges module to resolve fees by product_code.
INSERT INTO product_charges (charge_definition_id, product_code, created_at, created_by, updated_at, updated_by)
SELECT DISTINCT
    cd.id,
    p.code,
    CURRENT_TIMESTAMP,
    'migration',
    CURRENT_TIMESTAMP,
    'migration'
FROM product_fee_entries pfe
JOIN product_versions pv ON pv.id = pfe.product_version_id
JOIN products p ON p.id = pv.product_id
JOIN charge_definitions cd ON cd.name = CONCAT(pfe.fee_type, ' - ', p.code)
WHERE NOT EXISTS (
    SELECT 1 FROM product_charges pc
    WHERE pc.charge_definition_id = cd.id
    AND pc.product_code = p.code
);
