<?php
require_once __DIR__ . "/db.php";

function add_package(string $trackCode, string $phoneNumber, string $shelf, float $price, string $createdBy): bool {
    $conn = get_db("employee");
    $stmt = $conn->prepare("INSERT INTO packages (trackCode, phoneNumber, shelf, price, createdBy, isDeleted, createdAt) VALUES (?, ?, ?, ?, ?, 0, NOW())");
    if (!$stmt) {
        return false;
    }
    $stmt->bind_param("sssds", $trackCode, $phoneNumber, $shelf, $price, $createdBy);
    $ok = $stmt->execute();
    $stmt->close();
    $conn->close();
    return $ok;
}

function mark_picked(int $packageId): bool {
    $conn = get_db("employee");
    $stmt = $conn->prepare("UPDATE packages SET isDeleted = 1 WHERE packageID = ? AND isDeleted = 0");
    if (!$stmt) {
        return false;
    }
    $stmt->bind_param("i", $packageId);
    $ok = $stmt->execute();
    $stmt->close();
    $conn->close();
    return $ok;
}

function search_packages(string $query): array {
    $conn = get_db("employee");
    $like = "%" . $query . "%";
    $stmt = $conn->prepare("SELECT packageID, trackCode, phoneNumber, shelf, price, isDeleted, createdAt FROM packages WHERE trackCode LIKE ? OR phoneNumber LIKE ? ORDER BY createdAt DESC LIMIT 100");
    if (!$stmt) {
        return [];
    }
    $stmt->bind_param("ss", $like, $like);
    $stmt->execute();
    $result = $stmt->get_result();
    $rows = $result ? $result->fetch_all(MYSQLI_ASSOC) : [];
    $stmt->close();
    $conn->close();
    return $rows;
}

function list_packages_by_phone(string $phoneNumber): array {
    $conn = get_db("customer");
    $stmt = $conn->prepare("SELECT trackCode, phoneNumber, shelf, price, isDeleted, createdAt FROM packages WHERE phoneNumber = ? ORDER BY createdAt DESC");
    if (!$stmt) {
        return [];
    }
    $stmt->bind_param("s", $phoneNumber);
    $stmt->execute();
    $result = $stmt->get_result();
    $rows = $result ? $result->fetch_all(MYSQLI_ASSOC) : [];
    $stmt->close();
    $conn->close();
    return $rows;
}

function list_top_packages(string $sort, bool $showDeleted): array {
    $conn = get_db("employee");
    $orderBy = "createdAt DESC";

    if ($sort === "price") {
        $orderBy = "price DESC";
    } elseif ($sort === "oldest") {
        $orderBy = "createdAt ASC";
    } elseif ($sort === "newest") {
        $orderBy = "createdAt DESC";
    }

    $isDeleted = $showDeleted ? 1 : 0;
    $stmt = $conn->prepare("SELECT packageID, trackCode, phoneNumber, shelf, price, isDeleted, createdAt FROM packages WHERE isDeleted = ? ORDER BY $orderBy LIMIT 50");
    if (!$stmt) {
        return [];
    }
    $stmt->bind_param("i", $isDeleted);
    $stmt->execute();
    $result = $stmt->get_result();
    $rows = $result ? $result->fetch_all(MYSQLI_ASSOC) : [];
    $stmt->close();
    $conn->close();
    return $rows;
}

function public_search_packages(string $query): array {
    $conn = get_db("web");
    $stmt = $conn->prepare("SELECT trackCode, price FROM packages WHERE isDeleted = 0 AND BINARY trackCode = ? ORDER BY createdAt DESC");
    if (!$stmt) {
        return [];
    }
    $stmt->bind_param("s", $query);
    $stmt->execute();
    $result = $stmt->get_result();
    $rows = $result ? $result->fetch_all(MYSQLI_ASSOC) : [];
    $stmt->close();
    $conn->close();
    return $rows;
}

function list_packages_admin(int $limit = 50, string $dbRole = "admin"): array {
    $conn = get_db($dbRole);
    $stmt = $conn->prepare("SELECT packageID, trackCode, phoneNumber, shelf, price, isDeleted, createdBy, createdAt FROM packages ORDER BY createdAt DESC LIMIT ?");
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
