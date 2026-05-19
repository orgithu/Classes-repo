<?php
require_once __DIR__ . "/db.php";

function create_customer(string $username, string $email, string $phone, string $passwordHash): bool {
    $conn = get_db("customer");
    $stmt = $conn->prepare("INSERT INTO customers (username, email, phoneNumber, passwordHash, createdAt) VALUES (?, ?, ?, ?, NOW())");
    if (!$stmt) {
        return false;
    }
    $stmt->bind_param("ssss", $username, $email, $phone, $passwordHash);
    $ok = $stmt->execute();
    $stmt->close();
    $conn->close();
    return $ok;
}

function find_customer_by_username(string $username): ?array {
    $conn = get_db("customer");
    $stmt = $conn->prepare("SELECT customerID, username, email, phoneNumber, passwordHash FROM customers WHERE username = ? LIMIT 1");
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

function list_customers(int $limit = 50, string $dbRole = "admin"): array {
    $conn = get_db($dbRole);
    $stmt = $conn->prepare("SELECT customerID, username, email, phoneNumber, createdAt FROM customers ORDER BY createdAt DESC LIMIT ?");
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
