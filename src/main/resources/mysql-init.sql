CREATE TABLE IF NOT EXISTS clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS contracts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contract_number VARCHAR(255) NOT NULL,
    details TEXT,
    client_id BIGINT,
    INDEX(contract_number),
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_date DATE NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    payment_type VARCHAR(50) NOT NULL,
    contract_number VARCHAR(255) NOT NULL,
    FOREIGN KEY (contract_number) REFERENCES contracts(contract_number)
);

-- Disable foreign key checks to allow truncating tables with foreign key constraints
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the tables
TRUNCATE TABLE payments;
TRUNCATE TABLE contracts;
TRUNCATE TABLE clients;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO clients (name, email) VALUES
('Alice Wonderland', 'alice@example.com'),
('Bob Builder', 'bob@example.com'),
('Csv', 'csv@example.com'),
('Xml', 'xml@example.com');


-- Contracts for Alice (assuming Alice's ID is 1)
INSERT INTO contracts (contract_number, details, client_id) VALUES
('A-001', 'Alice Contract 1', 1),
('A-002', 'Alice Contract 2', 1);

-- Contracts for Bob (assuming Bob's ID is 2)
INSERT INTO contracts (contract_number, details, client_id) VALUES
('B-001', 'Bob Contract 1', 2),
('B-002', 'Bob Contract 2', 2);

-- Contracts for Csv (assuming Csv's ID is 3)
INSERT INTO contracts (contract_number, details, client_id) VALUES
('Csv-12345', 'Csv Contract 1', 3),
('Csv-54321', 'Csv Contract 2', 3);

-- Contracts for Xml (assuming Xml's ID is 4)
INSERT INTO contracts (contract_number, details, client_id) VALUES
('Xml-67890', 'Xml Contract 1', 4),
('Xml-09876', 'Xml Contract 2', 4);

-- Payments for Alice's contracts
INSERT INTO payments (payment_date, amount, payment_type, contract_number) VALUES
('2024-03-01', 1200.00, 'INCOMING', 'A-001'),
('2024-03-02', 300.00, 'OUTGOING', 'A-001'),
('2024-03-03', 1500.00, 'INCOMING', 'A-002'),
('2024-03-04', 450.00, 'OUTGOING', 'A-002');

-- Payments for Bob's contracts
INSERT INTO payments (payment_date, amount, payment_type, contract_number) VALUES
('2024-04-01', 1000.00, 'INCOMING', 'B-001'),
('2024-04-02', 200.00, 'OUTGOING', 'B-001'),
('2024-04-03', 1800.00, 'INCOMING', 'B-002'),
('2024-04-04', 400.00, 'OUTGOING', 'B-002');
