-- V21: Add MFA secret column to users table
ALTER TABLE users ADD COLUMN mfa_secret VARCHAR(255);