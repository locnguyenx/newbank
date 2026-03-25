-- V28__add_full_name_to_users.sql
-- Add full_name column to users table

ALTER TABLE users ADD COLUMN full_name VARCHAR(255);
