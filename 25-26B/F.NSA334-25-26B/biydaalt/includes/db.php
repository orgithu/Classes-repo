<?php
$DB_HOST = "127.0.0.1";
$DB_NAME = "biydaalt";

$DB_USERS = [
    "web" => ["user" => "db_web", "pass" => "pass123"],
    "customer" => ["user" => "db_customer_app", "pass" => "pass123"],
    "employee" => ["user" => "db_employee", "pass" => "pass123"],
    "admin" => ["user" => "db_admin", "pass" => "pass123"],
];

function get_db(string $role): mysqli {
    global $DB_HOST, $DB_NAME, $DB_USERS;
    $config = $DB_USERS[$role] ?? $DB_USERS["web"];
    $conn = new mysqli($DB_HOST, $config["user"], $config["pass"], $DB_NAME);
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }
    $conn->set_charset("utf8mb4");
    return $conn;
}
