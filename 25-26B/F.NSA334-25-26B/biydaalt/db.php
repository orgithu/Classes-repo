<?php
declare(strict_types=1);

function db(): mysqli
{
    static $conn = null;

    if ($conn instanceof mysqli) {
        return $conn;
    }

    mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
    $conn = new mysqli("127.0.0.1", "guest", "pass123", "biydaalt");
    $conn->set_charset("utf8mb4");

    bootstrapSchema($conn);

    return $conn;
}

function bootstrapSchema(mysqli $conn): void
{
    $conn->query(
        "CREATE TABLE IF NOT EXISTS employees (
            employeeID INT AUTO_INCREMENT PRIMARY KEY,
            employeeName VARCHAR(100) NOT NULL UNIQUE,
            password VARCHAR(64) NOT NULL
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
    );

    $conn->query(
        "CREATE TABLE IF NOT EXISTS packages (
            packageID INT AUTO_INCREMENT PRIMARY KEY,
            trackCode VARCHAR(40) NOT NULL,
            phoneNumber VARCHAR(20) NOT NULL,
            shelf VARCHAR(50) DEFAULT NULL,
            createdBy INT DEFAULT NULL,
            isDeleted TINYINT(1) NOT NULL DEFAULT 0,
            status VARCHAR(20) NOT NULL DEFAULT 'Pending',
            price DECIMAL(12,2) NOT NULL DEFAULT 0,
            createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
            arrivedAt DATETIME DEFAULT NULL
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
    );

    $conn->query(
        "CREATE TABLE IF NOT EXISTS admin_auth_tokens (
            token VARCHAR(64) PRIMARY KEY,
            employeeID INT NOT NULL,
            expiresAt DATETIME NOT NULL,
            createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
    );

    ensureColumn($conn, "packages", "arrivedAt", "ALTER TABLE packages ADD COLUMN arrivedAt DATETIME DEFAULT NULL");
    ensureColumn($conn, "packages", "createdAt", "ALTER TABLE packages ADD COLUMN createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP");
    ensureColumn($conn, "packages", "status", "ALTER TABLE packages ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'Pending'");
    ensureColumn($conn, "packages", "price", "ALTER TABLE packages ADD COLUMN price DECIMAL(12,2) NOT NULL DEFAULT 0");
    ensureColumn($conn, "packages", "isDeleted", "ALTER TABLE packages ADD COLUMN isDeleted TINYINT(1) NOT NULL DEFAULT 0");
    ensureColumn($conn, "packages", "shelf", "ALTER TABLE packages ADD COLUMN shelf VARCHAR(50) DEFAULT NULL");
    ensureColumn($conn, "packages", "createdBy", "ALTER TABLE packages ADD COLUMN createdBy INT DEFAULT NULL");
    ensureColumn($conn, "packages", "trackCode", "ALTER TABLE packages ADD COLUMN trackCode VARCHAR(40) NOT NULL");
    ensureColumn($conn, "packages", "phoneNumber", "ALTER TABLE packages ADD COLUMN phoneNumber VARCHAR(20) NOT NULL");

    ensureEmployeePasswordColumn($conn);
    migrateEmployeePasswordsToMd5($conn);
    seedDefaultEmployeeIfNeeded($conn);
    seedExamplePackages($conn);
}

function ensureColumn(mysqli $conn, string $table, string $column, string $alterSql): void
{
    $stmt = $conn->prepare(
        "SELECT COUNT(*)
         FROM information_schema.COLUMNS
         WHERE TABLE_SCHEMA = DATABASE()
           AND TABLE_NAME = ?
           AND COLUMN_NAME = ?"
    );
    $stmt->bind_param("ss", $table, $column);
    $stmt->execute();
    $stmt->bind_result($exists);
    $stmt->fetch();
    $stmt->close();

    if ((int)$exists === 0) {
        $conn->query($alterSql);
    }
}

function migrateEmployeePasswordsToMd5(mysqli $conn): void
{
    $result = $conn->query("SELECT employeeID, password FROM employees");
    $updateStmt = $conn->prepare("UPDATE employees SET password = ? WHERE employeeID = ?");

    while ($row = $result->fetch_assoc()) {
        $password = (string)$row["password"];
        $employeeId = (int)$row["employeeID"];

        if (!preg_match('/^[a-f0-9]{32}$/i', $password)) {
            $hashed = md5($password);
            $updateStmt->bind_param("si", $hashed, $employeeId);
            $updateStmt->execute();
        }
    }

    $updateStmt->close();
}

function ensureEmployeePasswordColumn(mysqli $conn): void
{
    $stmt = $conn->prepare(
        "SELECT CHARACTER_MAXIMUM_LENGTH
         FROM information_schema.COLUMNS
         WHERE TABLE_SCHEMA = DATABASE()
           AND TABLE_NAME = 'employees'
           AND COLUMN_NAME = 'password'
         LIMIT 1"
    );
    $stmt->execute();
    $stmt->bind_result($length);
    $stmt->fetch();
    $stmt->close();

    if ((int)$length < 32) {
        $conn->query("ALTER TABLE employees MODIFY COLUMN password VARCHAR(64) NOT NULL");
    }
}

function seedDefaultEmployeeIfNeeded(mysqli $conn): void
{
    $result = $conn->query("SELECT COUNT(*) AS total FROM employees");
    $row = $result->fetch_assoc();
    $total = (int)($row["total"] ?? 0);

    if ($total === 0) {
        $username = "admin_user";
        $password = md5("securePass123");
        $stmt = $conn->prepare("INSERT INTO employees (employeeName, password) VALUES (?, ?)");
        $stmt->bind_param("ss", $username, $password);
        $stmt->execute();
        $stmt->close();
    }
}

function seedExamplePackages(mysqli $conn): void
{
    $countResult = $conn->query("SELECT COUNT(*) AS total FROM packages WHERE trackCode LIKE 'DEMO%'");
    $countRow = $countResult->fetch_assoc();
    $demoTotal = (int)($countRow["total"] ?? 0);
    if ($demoTotal >= 10) {
        return;
    }

    $employeeResult = $conn->query("SELECT employeeID FROM employees ORDER BY employeeID ASC LIMIT 1");
    $employeeRow = $employeeResult->fetch_assoc();
    $createdBy = (int)($employeeRow["employeeID"] ?? 1);

    $samples = [
        ["DEMO1001", "99110001", "A-1", "Arrived", 8500.00, "2026-04-10 09:10:00", "2026-04-10 09:10:00", 0],
        ["DEMO1002", "99110001", "A-2", "Arrived", 9200.00, "2026-04-11 10:30:00", "2026-04-11 10:30:00", 0],
        ["DEMO1003", "99112222", "B-1", "Pending", 7000.00, null, "2026-04-12 11:00:00", 0],
        ["DEMO1004", "99113333", "B-2", "Arrived", 11500.00, "2026-04-13 14:20:00", "2026-04-13 14:20:00", 0],
        ["DEMO1005", "99114444", "C-1", "Pending", 5300.00, null, "2026-04-14 15:40:00", 0],
        ["DEMO1006", "99115555", "C-2", "Arrived", 14900.00, "2026-04-15 08:05:00", "2026-04-15 08:05:00", 0],
        ["DEMO1007", "99116666", "D-1", "Arrived", 6300.00, "2026-04-15 16:55:00", "2026-04-15 16:55:00", 0],
        ["DEMO1008", "99117777", "D-2", "Pending", 10400.00, null, "2026-04-16 09:45:00", 0],
        ["DEMO1009", "99118888", "E-1", "Arrived", 7800.00, "2026-04-16 13:15:00", "2026-04-16 13:15:00", 0],
        ["DEMO1010", "99119999", "E-2", "Arrived", 9950.00, "2026-04-17 10:10:00", "2026-04-17 10:10:00", 0],
    ];

    $existsStmt = $conn->prepare("SELECT packageID FROM packages WHERE trackCode = ? LIMIT 1");
    $insertStmt = $conn->prepare(
        "INSERT INTO packages (
            trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
    );

    foreach ($samples as $sample) {
        [$trackCode, $phoneNumber, $shelf, $status, $price, $arrivedAt, $createdAt, $isDeleted] = $sample;

        $existsStmt->bind_param("s", $trackCode);
        $existsStmt->execute();
        $exists = $existsStmt->get_result()->fetch_assoc();
        if ($exists) {
            continue;
        }

        $insertStmt->bind_param(
            "sssisdssi",
            $trackCode,
            $phoneNumber,
            $shelf,
            $createdBy,
            $status,
            $price,
            $arrivedAt,
            $createdAt,
            $isDeleted
        );
        $insertStmt->execute();
    }

    $existsStmt->close();
    $insertStmt->close();
}

function h(string $value): string
{
    return htmlspecialchars($value, ENT_QUOTES, "UTF-8");
}
