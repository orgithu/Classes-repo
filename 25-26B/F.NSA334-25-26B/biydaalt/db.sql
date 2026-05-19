CREATE DATABASE IF NOT EXISTS biydaalt CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biydaalt;

DROP TABLE IF EXISTS packages;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS customers;

CREATE TABLE IF NOT EXISTS customers (
    customerID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(8) NOT NULL,
    passwordHash VARCHAR(255) NOT NULL,
    createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS employees (
    employeeID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    phoneNumber VARCHAR(8) NOT NULL,
    passwordHash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'employee',
    createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS packages (
    packageID INT AUTO_INCREMENT PRIMARY KEY,
    trackCode VARCHAR(40) NOT NULL,
    phoneNumber VARCHAR(8) NOT NULL,
    shelf VARCHAR(50) NOT NULL,
    price DECIMAL(12,2) NOT NULL DEFAULT 0,
    isDeleted TINYINT(1) NOT NULL DEFAULT 0,
    createdBy VARCHAR(50) NOT NULL,
    createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_packages_trackCode ON packages(trackCode);
CREATE INDEX idx_packages_phoneNumber ON packages(phoneNumber);
CREATE INDEX idx_customers_username ON customers(username);
CREATE INDEX idx_employees_username ON employees(username);

-- Database users and privileges
CREATE USER IF NOT EXISTS 'db_admin'@'localhost' IDENTIFIED BY 'pass123';
CREATE USER IF NOT EXISTS 'db_web'@'localhost' IDENTIFIED BY 'pass123';
CREATE USER IF NOT EXISTS 'db_customer_app'@'localhost' IDENTIFIED BY 'pass123';
CREATE USER IF NOT EXISTS 'db_employee'@'localhost' IDENTIFIED BY 'pass123';

GRANT ALL PRIVILEGES ON biydaalt.* TO 'db_admin'@'localhost';

GRANT SELECT ON biydaalt.packages TO 'db_web'@'localhost';

GRANT SELECT ON biydaalt.packages TO 'db_customer_app'@'localhost';
GRANT SELECT, INSERT ON biydaalt.customers TO 'db_customer_app'@'localhost';

GRANT SELECT, INSERT, UPDATE ON biydaalt.packages TO 'db_employee'@'localhost';
GRANT SELECT ON biydaalt.employees TO 'db_employee'@'localhost';

FLUSH PRIVILEGES;

INSERT INTO packages (trackCode, phoneNumber, shelf, price, isDeleted, createdBy, createdAt)
SELECT 'SF1235123523', '99110001', 'A-1', 8500.00, 0, 'admin', '2026-04-10 09:10:00'
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF1235123523');

INSERT INTO packages (trackCode, phoneNumber, shelf, price, isDeleted, createdBy, createdAt)
SELECT 'SF123512351', '99110001', 'A-2', 9200.00, 0, 'admin', '2026-04-11 10:30:00'
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF123512351');

INSERT INTO packages (trackCode, phoneNumber, shelf, price, isDeleted, createdBy, createdAt)
SELECT 'SF12351232346', '99112222', 'B-1', 7000.00, 0, 'admin', '2026-04-12 11:00:00'
WHERE NOT EXISTS (SELECT 1 FROM packages WHERE trackCode = 'SF12351232346');
