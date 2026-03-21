INSERT INTO currencies (code, name, symbol, decimal_places, is_active, created_by) VALUES
('USD', 'US Dollar', '$', 2, true, 'system'),
('EUR', 'Euro', '€', 2, true, 'system'),
('GBP', 'British Pound', '£', 2, true, 'system'),
('SGD', 'Singapore Dollar', 'S$', 2, true, 'system'),
('JPY', 'Japanese Yen', '¥', 0, true, 'system'),
('CAD', 'Canadian Dollar', 'C$', 2, true, 'system'),
('AUD', 'Australian Dollar', 'A$', 2, true, 'system'),
('CHF', 'Swiss Franc', 'CHF', 2, true, 'system'),
('CNY', 'Chinese Yuan', '¥', 2, true, 'system'),
('HKD', 'Hong Kong Dollar', 'HK$', 2, true, 'system');

INSERT INTO countries (iso_code, name, region, is_active, created_by) VALUES
('US', 'United States', 'AMERICAS', true, 'system'),
('GB', 'United Kingdom', 'EUROPE', true, 'system'),
('SG', 'Singapore', 'ASIA', true, 'system'),
('HK', 'Hong Kong', 'ASIA', true, 'system'),
('JP', 'Japan', 'ASIA', true, 'system'),
('CN', 'China', 'ASIA', true, 'system'),
('DE', 'Germany', 'EUROPE', true, 'system'),
('FR', 'France', 'EUROPE', true, 'system'),
('AU', 'Australia', 'OCEANIA', true, 'system'),
('CA', 'Canada', 'AMERICAS', true, 'system');

INSERT INTO channels (code, name, is_active, created_by) VALUES
('BRANCH', 'Branch', true, 'system'),
('ONLINE', 'Online Banking', true, 'system'),
('MOBILE', 'Mobile Banking', true, 'system'),
('ATM', 'ATM', true, 'system'),
('PHONE', 'Phone Banking', true, 'system');

INSERT INTO document_types (code, name, category, is_active, created_by) VALUES
('PASSPORT', 'Passport', 'IDENTITY', true, 'system'),
('DRIVERS_LICENSE', 'Drivers License', 'IDENTITY', true, 'system'),
('UTILITY_BILL', 'Utility Bill', 'ADDRESS', true, 'system'),
('INC_CERT', 'Certificate of Incorporation', 'CORPORATE', true, 'system'),
('ARTICLES_OF_INC', 'Articles of Incorporation', 'CORPORATE', true, 'system');
