-- V23: Create amount_thresholds table for approval limits
CREATE TABLE amount_thresholds (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role VARCHAR(30) NOT NULL,
    threshold DECIMAL(19, 4) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE INDEX idx_amount_thresholds_user_id ON amount_thresholds(user_id);
CREATE INDEX idx_amount_thresholds_role ON amount_thresholds(role);