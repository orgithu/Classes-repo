<?php
require_once __DIR__ . "/db.php";

function count_employees(): int {
    $conn = get_db("employee");
    $result = $conn->query("SELECT COUNT(*) AS total FROM employees");
    $row = $result ? $result->fetch_assoc() : ["total" => 0];
    $conn->close();
    return (int)($row["total"] ?? 0);
}

function create_employee(string $username, string $phone, string $passwordHash, string $role, string $dbRole = "employee"): bool {
    $conn = get_db($dbRole);
    $stmt = $conn->prepare("INSERT INTO employees (username, phoneNumber, passwordHash, role, createdAt) VALUES (?, ?, ?, ?, NOW())");
    if (!$stmt) {
        return false;
    }
    $stmt->bind_param("ssss", $username, $phone, $passwordHash, $role);
    $ok = $stmt->execute();
    $stmt->close();
    $conn->close();
    return $ok;
}

function find_employee_by_username(string $username): ?array {
    $conn = get_db("employee");
    $stmt = $conn->prepare("SELECT employeeID, username, phoneNumber, passwordHash, role FROM employees WHERE username = ? LIMIT 1");
    if (!$stmt) {
        return null;
    }
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();
    $row = $result ? $result->fetch_assoc() : null;
    $stmt->close();
    $conn->close();
    return $row ?: null;
}

function list_employees(int $limit = 50, string $dbRole = "admin"): array {
    $conn = get_db($dbRole);
    $stmt = $conn->prepare("SELECT employeeID, username, phoneNumber, role, createdAt FROM employees ORDER BY createdAt DESC LIMIT ?");
    if (!$stmt) {
        return [];
    }
    $stmt->bind_param("i", $limit);
    $stmt->execute();
    $result = $stmt->get_result();
    $rows = $result ? $result->fetch_all(MYSQLI_ASSOC) : [];
    $stmt->close();
    $conn->close();
    return $rows;
}
