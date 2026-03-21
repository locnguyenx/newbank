-- V15__add_charge_rules_unique_constraint.sql
-- Add unique constraint on charge_rules to prevent ambiguous tier matching.
-- Without this constraint, duplicate rules for the same (definition, method, amount, rate)
-- can cause V14 migration to assign tiers to the wrong rule (silent data integrity issue).
-- Also prevents new duplicates from being created going forward.

CREATE UNIQUE INDEX IF NOT EXISTS uk_charge_rules_definition_method_amount_rate
    ON charge_rules (charge_definition_id, calculation_method, flat_amount, percentage_rate);
