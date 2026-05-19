<?php
function clean_input(string $data): string {
    $data = trim($data);
    $data = stripslashes($data);
    return htmlspecialchars($data, ENT_QUOTES, "UTF-8");
}

function validate_phone(string $phone): bool {
    return preg_match('/^[0-9]{8}$/', $phone) === 1;
}

function validate_username(string $username): bool {
    return preg_match('/^[a-zA-Z0-9_]{3,20}$/', $username) === 1;
}

function validate_password(string $password): bool {
    return strlen($password) >= 6;
}

function validate_track_code(string $trackCode): bool {
    return $trackCode !== "" && strlen($trackCode) <= 40;
}
