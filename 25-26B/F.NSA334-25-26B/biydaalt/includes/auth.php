<?php
function require_customer(): void {
    if (!isset($_SESSION["customer_id"])) {
        header("Location: login.php");
        exit;
    }
}

function require_employee(): void {
    if (!isset($_SESSION["employee_id"])) {
        header("Location: admin.php");
        exit;
    }
}
