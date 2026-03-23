-- V22: Create user_scopes table for RBAC
CREATE TABLE user_scopes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    scope_type VARCHAR(20) NOT NULL,
    scope_id BIGINT,
    role VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    CONSTRAINT uk_user_scopes UNIQUE (user_id, scope_type, scope_id, role)
);

CREATE INDEX idx_user_scopes_user_id ON user_scopes(user_id);
CREATE INDEX idx_user_scopes_scope_type ON user_scopes(scope_type);
CREATE INDEX idx_user_scopes_scope_id ON user_scopes(scope_id);