<?php
declare(strict_types=1);

function db(): mysqli
{
    static $conn = null;
    static $bootstrapped = false;

    if ($conn instanceof mysqli) {
        return $conn;
    }

    mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

    $host = getenv("DB_HOST") ?: "127.0.0.1";
    $user = getenv("DB_USER") ?: "guest";
    $pass = getenv("DB_PASS") ?: "pass123";
    $name = getenv("DB_NAME") ?: "biydaalt";

    $database = preg_replace('/[^a-zA-Z0-9_]/', "", $name) ?? "";
    if ($database === "") {
        throw new RuntimeException("Invalid database name.");
    }

    $conn = new mysqli($host, $user, $pass);
    $conn->set_charset("utf8mb4");
    $conn->query("CREATE DATABASE IF NOT EXISTS `{$database}` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
    $conn->select_db($database);

    if (!$bootstrapped) {
        bootstrapFromSql($conn, __DIR__ . "/db.sql", $database);
        $bootstrapped = true;
    }

    return $conn;
}

function bootstrapFromSql(mysqli $conn, string $sqlPath, string $database): void
{
    if (!is_file($sqlPath)) {
        throw new RuntimeException("Missing db.sql file.");
    }

    $sql = file_get_contents($sqlPath);
    if ($sql === false) {
        throw new RuntimeException("Unable to read db.sql file.");
    }

    $sql = str_replace("__DB_NAME__", "`{$database}`", $sql);

    if (!$conn->multi_query($sql)) {
        throw new RuntimeException("Failed to run db.sql: " . $conn->error);
    }

    do {
        $result = $conn->store_result();
        if ($result instanceof mysqli_result) {
            $result->free();
        }
    } while ($conn->more_results() && $conn->next_result());

    if ($conn->errno !== 0) {
        throw new RuntimeException("Error after running db.sql: " . $conn->error);
    }
}

function h(string $value): string
{
    return htmlspecialchars($value, ENT_QUOTES, "UTF-8");
}
