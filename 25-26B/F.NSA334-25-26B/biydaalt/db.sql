CREATE DATABASE IF NOT EXISTS __DB_NAME__ CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE __DB_NAME__;

CREATE TABLE IF NOT EXISTS employees (
    employeeID INT AUTO_INCREMENT PRIMARY KEY,
    employeeName VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS packages (
    packageID INT AUTO_INCREMENT PRIMARY KEY,
    trackCode VARCHAR(40) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL,
    shelf VARCHAR(50) DEFAULT NULL,
    createdBy INT DEFAULT NULL,
    isDeleted TINYINT(1) NOT NULL DEFAULT 0,
    price DECIMAL(12,2) NOT NULL DEFAULT 0,
    createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE employees MODIFY COLUMN password VARCHAR(64) NOT NULL;
ALTER TABLE packages ADD COLUMN IF NOT EXISTS trackCode VARCHAR(40) NOT NULL DEFAULT '';
ALTER TABLE packages ADD COLUMN IF NOT EXISTS phoneNumber VARCHAR(20) NOT NULL DEFAULT '';
ALTER TABLE packages ADD COLUMN IF NOT EXISTS shelf VARCHAR(50) DEFAULT NULL;
ALTER TABLE packages ADD COLUMN IF NOT EXISTS createdBy INT DEFAULT NULL;
ALTER TABLE packages ADD COLUMN IF NOT EXISTS isDeleted TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE packages ADD COLUMN IF NOT EXISTS price DECIMAL(12,2) NOT NULL DEFAULT 0;
ALTER TABLE packages ADD COLUMN IF NOT EXISTS createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

UPDATE employees
SET password = MD5(password)
WHERE password NOT REGEXP '^[a-fA-F0-9]{32}$';

INSERT INTO employees (employeeName, password)
SELECT 'admin', MD5('pass123')
WHERE NOT EXISTS (
    SELECT 1 FROM employees WHERE employeeName = 'admin'
);

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF1235123523', '99110001', 'A-1', 1, 'Arrived', 8500.00, '2026-04-10 09:10:00', '2026-04-10 09:10:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF1235123523');

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF123512351', '99110001', 'A-2', 1, 'Arrived', 9200.00, '2026-04-11 10:30:00', '2026-04-11 10:30:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF123512351');

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF12351232346', '99112222', 'B-1', 1, 'Pending', 7000.00, NULL, '2026-04-12 11:00:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF12351232346');

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF23435123523', '99113333', 'B-2', 1, 'Arrived', 11500.00, '2026-04-13 14:20:00', '2026-04-13 14:20:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF23435123523');

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF2343511005', '99114444', 'C-1', 1, 'Pending', 5300.00, NULL, '2026-04-14 15:40:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF2343511005');

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF2343511006', '99115555', 'C-2', 1, 'Arrived', 14900.00, '2026-04-15 08:05:00', '2026-04-15 08:05:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF2343511006');

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF2343511007', '99116666', 'D-1', 1, 'Arrived', 6300.00, '2026-04-15 16:55:00', '2026-04-15 16:55:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF2343511007');

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF2343511008', '99117777', 'D-2', 1, 'Pending', 10400.00, NULL, '2026-04-16 09:45:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF2343511008');

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF2343511009', '99118888', 'E-1', 1, 'Arrived', 7800.00, '2026-04-16 13:15:00', '2026-04-16 13:15:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF2343511009');

INSERT INTO packages (trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted)
SELECT 'SF2343511010', '99119999', 'E-2', 1, 'Arrived', 9950.00, '2026-04-17 10:10:00', '2026-04-17 10:10:00', 0
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF2343511010');
