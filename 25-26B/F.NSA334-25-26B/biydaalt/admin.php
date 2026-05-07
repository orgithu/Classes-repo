<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

session_start();

require_once __DIR__ . "/includes/db.php";
require_once __DIR__ . "/includes/validation.php";
require_once __DIR__ . "/includes/employee.php";
require_once __DIR__ . "/includes/customer.php";
require_once __DIR__ . "/includes/package.php";

global $DB_USERS;
$adminUser = $DB_USERS["admin"]["user"] ?? "db_admin";
$adminPass = $DB_USERS["admin"]["pass"] ?? "pass123";

$errors = [];
$success = "";
$sqlText = "";
$sqlOutput = "";
$sqlError = "";

$isAdmin = $_SESSION["admin_auth"] ?? false;

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $action = $_POST["action"] ?? "";

    if ($action === "login_admin") {
        $username = clean_input($_POST["username"] ?? "");
        $password = $_POST["password"] ?? "";

        if ($username === $adminUser && $password === $adminPass) {
            $_SESSION["admin_auth"] = true;
            $isAdmin = true;
            $success = "Admin login successful!";
        } else {
            $errors[] = "Invalid admin credentials.";
        }
    }

    if ($action === "logout_admin") {
        session_destroy();
        header("Location: admin.php");
        exit;
    }

    if ($isAdmin && $action === "create_employee") {
        $username = clean_input($_POST["username"] ?? "");
        $phone = clean_input($_POST["phoneNumber"] ?? "");
        $role = clean_input($_POST["role"] ?? "employee");
        $password = $_POST["password"] ?? "";

        if (!validate_username($username) || !validate_phone($phone) || !validate_password($password) || !in_array($role, ["employee", "admin"], true)) {
            $errors[] = "Please provide valid employee details.";
        } else {
            $passwordHash = password_hash($password, PASSWORD_DEFAULT);
            if (create_employee($username, $phone, $passwordHash, $role, "admin")) {
                $success = "Employee account created.";
            } else {
                $errors[] = "Could not create employee.";
            }
        }
    }

    if ($isAdmin && $action === "sql_query") {
        $sqlText = trim($_POST["sqlText"] ?? "");
        if ($sqlText !== "") {
            $conn = get_db("admin");
            $result = $conn->query($sqlText);
            if ($result === false) {
                $sqlError = "SQL Error: " . $conn->error;
            } else {
                if ($result instanceof mysqli_result) {
                    $sqlOutput = "<table border='1' style='border-collapse: collapse;'>";
                    $fields = $result->fetch_fields();
                    $sqlOutput .= "<tr>";
                    foreach ($fields as $field) {
                        $sqlOutput .= "<th style='padding: 8px; background: #eee;'>" . htmlspecialchars($field->name) . "</th>";
                    }
                    $sqlOutput .= "</tr>";
                    while ($row = $result->fetch_assoc()) {
                        $sqlOutput .= "<tr>";
                        foreach ($row as $value) {
                            $sqlOutput .= "<td style='padding: 8px;'>" . htmlspecialchars((string)($value ?? 'NULL')) . "</td>";
                        }
                        $sqlOutput .= "</tr>";
                    }
                    $sqlOutput .= "</table>";
                } else {
                    $sqlOutput = "Query successful. Affected rows: " . $conn->affected_rows;
                }
            }
            $conn->close();
        }
    }
}

$employees = $isAdmin ? list_employees(50, "admin") : [];
$customers = $isAdmin ? list_customers(50, "admin") : [];
$packages = $isAdmin ? list_packages_admin(50, "admin") : [];
?>
<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DB Admin</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 980px; margin: 30px auto; padding: 0 12px; }
        fieldset { margin-bottom: 20px; border: 1px solid #d5d5d5; padding: 16px; }
        label { display: block; margin: 8px 0 4px; font-weight: 600; }
        input, button, select { width: 100%; padding: 8px; box-sizing: border-box; }
        button { cursor: pointer; margin-top: 10px; }
        .ok { color: #0b7a29; margin: 10px 0; }
        .err { color: #b00020; margin: 6px 0; }
        table { width: 100%; border-collapse: collapse; margin-top: 14px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        .inline-form { margin: 0; }
        .inline-form button { width: auto; padding: 6px 10px; margin: 0; }
        .muted { color: #666; font-size: 0.9rem; }
    </style>
</head>
<body>
    <?php if ($success !== ""): ?>
        <p class="ok"><?= $success ?></p>
    <?php endif; ?>

    <?php foreach ($errors as $error): ?>
        <p class="err"><?= $error ?></p>
    <?php endforeach; ?>

    <!-- LOGIN DIV -->
    <?php if (!$isAdmin): ?>
    <div id="login-div">
        <h1>DB Admin Login</h1>
        <fieldset>
            <form method="post" action="admin.php">
                <input type="hidden" name="action" value="login_admin">

                <label for="username">Username</label>
                <input id="username" type="text" name="username" required>

                <label for="password">Password</label>
                <input id="password" type="password" name="password" required>

                <button type="submit">Login</button>
            </form>
        </fieldset>
    </div>
    <?php endif; ?>

    <!-- DASHBOARD DIV -->
    <?php if ($isAdmin): ?>
    <div id="dashboard-div">
        <h1>DB Admin Dashboard</h1>
        <form method="post" action="admin.php" style="margin-bottom: 20px;">
            <input type="hidden" name="action" value="logout_admin">
            <button type="submit">Logout</button>
        </form>

    <fieldset>
        <legend>Create Employee</legend>
        <form method="post" action="admin.php">
            <input type="hidden" name="action" value="create_employee">

            <label for="username">Username</label>
            <input id="username" type="text" name="username" required>

            <label for="phoneNumber">Phone Number (8 digits)</label>
            <input id="phoneNumber" type="text" name="phoneNumber" required>

            <label for="role">Role</label>
            <select id="role" name="role">
                <option value="employee">employee</option>
                <option value="admin">admin</option>
            </select>

            <label for="password">Password</label>
            <input id="password" type="password" name="password" required>

            <button type="submit">Create</button>
        </form>
    </fieldset>

    <fieldset>
        <legend>Employees (Top 50)</legend>
        <?php if ($employees): ?>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Phone</th>
                        <th>Role</th>
                        <th>Created</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($employees as $row): ?>
                        <tr>
                            <td><?= $row["employeeID"] ?></td>
                            <td><?= htmlspecialchars($row["username"]) ?></td>
                            <td><?= htmlspecialchars($row["phoneNumber"]) ?></td>
                            <td><?= htmlspecialchars($row["role"]) ?></td>
                            <td><?= htmlspecialchars($row["createdAt"]) ?></td>
                        </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        <?php else: ?>
            <p class="muted">No employees yet.</p>
        <?php endif; ?>
    </fieldset>

    <fieldset>
        <legend>Customers (Top 50)</legend>
        <?php if ($customers): ?>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Created</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($customers as $row): ?>
                        <tr>
                            <td><?= $row["customerID"] ?></td>
                            <td><?= htmlspecialchars($row["username"]) ?></td>
                            <td><?= htmlspecialchars($row["email"]) ?></td>
                            <td><?= htmlspecialchars($row["phoneNumber"]) ?></td>
                            <td><?= htmlspecialchars($row["createdAt"]) ?></td>
                        </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        <?php else: ?>
            <p class="muted">No customers yet.</p>
        <?php endif; ?>
    </fieldset>

    <fieldset>
        <legend>Packages (Top 50)</legend>
        <?php if ($packages): ?>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Track</th>
                        <th>Phone</th>
                        <th>Shelf</th>
                        <th>Price</th>
                        <th>isDeleted</th>
                        <th>Created By</th>
                        <th>Created</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($packages as $row): ?>
                        <tr>
                            <td><?= $row["packageID"] ?></td>
                            <td><?= htmlspecialchars($row["trackCode"]) ?></td>
                            <td><?= htmlspecialchars($row["phoneNumber"]) ?></td>
                            <td><?= htmlspecialchars($row["shelf"]) ?></td>
                            <td><?= number_format($row["price"], 2) ?></td>
                            <td><?= htmlspecialchars((string)$row["isDeleted"]) ?></td>
                            <td><?= htmlspecialchars($row["createdBy"]) ?></td>
                            <td><?= htmlspecialchars($row["createdAt"]) ?></td>
                        </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        <?php else: ?>
            <p class="muted">No packages yet.</p>
        <?php endif; ?>
    </fieldset>

    <fieldset>
        <legend>SQL Console</legend>
        <form method="post" action="admin.php">
            <input type="hidden" name="action" value="sql_query">
            <label for="sqlText">SQL</label>
            <input id="sqlText" type="text" name="sqlText" value="<?= htmlspecialchars($sqlText) ?>" required>
            <button type="submit">Run</button>
        </form>

        <?php if ($sqlError !== ""): ?>
            <div style="margin-top: 16px; color: #b00020;"><?= htmlspecialchars($sqlError) ?></div>
        <?php endif; ?>

        <?php if ($sqlOutput !== ""): ?>
            <div style="margin-top: 16px;">
                <?= $sqlOutput ?>
            </div>
        <?php endif; ?>
    </fieldset>

    </div>
    <?php endif; ?>
</body>
</html>
