-- V24: Create IAM tables for role management, login history, and failed login tracking

-- Role Definitions table
CREATE TABLE role_definitions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    is_system BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE INDEX idx_role_definitions_name ON role_definitions(name);
CREATE INDEX idx_role_definitions_is_system ON role_definitions(is_system);

-- Role Definition Permissions (element collection)
CREATE TABLE role_definition_permissions (
    role_id BIGINT NOT NULL REFERENCES role_definitions(id) ON DELETE CASCADE,
    permission VARCHAR(255) NOT NULL,
    PRIMARY KEY (role_id, permission)
);

-- Role Permissions table (for more granular permission management)
CREATE TABLE role_permissions (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    resource VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL
);

CREATE INDEX idx_role_permissions_role_id ON role_permissions(role_id);
CREATE INDEX idx_role_permissions_resource_action ON role_permissions(resource, action);

-- Login History table
CREATE TABLE login_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    login_type VARCHAR(50),
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    success BOOLEAN NOT NULL DEFAULT TRUE,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_login_history_user_id ON login_history(user_id);
CREATE INDEX idx_login_history_timestamp ON login_history(timestamp DESC);

-- Failed Login Attempts table
CREATE TABLE failed_login_attempts (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    ip_address VARCHAR(50),
    attempt_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reason VARCHAR(500)
);

CREATE INDEX idx_failed_login_attempts_email ON failed_login_attempts(email);
CREATE INDEX idx_failed_login_attempts_attempt_time ON failed_login_attempts(attempt_time DESC);

-- Seed default system roles
INSERT INTO role_definitions (name, description, is_system) VALUES 
('SYSTEM_ADMIN', 'Full system administration access', TRUE),
('COMPANY_ADMIN', 'Company-level administration', TRUE),
('DEPARTMENT_MAKER', 'Department-level transaction creator', TRUE),
('DEPARTMENT_CHECKER', 'Department-level transaction approver', TRUE),
('DEPARTMENT_VIEWER', 'Department-level read-only access', TRUE);

-- Add permissions to system roles
INSERT INTO role_definition_permissions (role_id, permission) 
SELECT id, '*' FROM role_definitions WHERE name = 'SYSTEM_ADMIN';

INSERT INTO role_definition_permissions (role_id, permission) 
SELECT id, 'iam:*' FROM role_definitions WHERE name = 'COMPANY_ADMIN';

INSERT INTO role_definition_permissions (role_id, permission) 
SELECT id, 'accounts:view' FROM role_definitions WHERE name = 'DEPARTMENT_VIEWER';
INSERT INTO role_definition_permissions (role_id, permission) 
SELECT id, 'transactions:create' FROM role_definitions WHERE name = 'DEPARTMENT_MAKER';
INSERT INTO role_definition_permissions (role_id, permission) 
SELECT id, 'transactions:approve' FROM role_definitions WHERE name = 'DEPARTMENT_CHECKER';