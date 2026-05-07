<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

session_start();

require_once __DIR__ . "/includes/validation.php";
require_once __DIR__ . "/includes/employee.php";
require_once __DIR__ . "/includes/package.php";

$errors = [];
$success = "";
$queryText = "";
$queryRows = [];
$topSort = "newest";
$topShowDeleted = false;
$topRows = [];

$employeeId = $_SESSION["employee_id"] ?? null;
$employeeUsername = $_SESSION["employee_username"] ?? null;
$employeeRole = $_SESSION["employee_role"] ?? null;
$isAuth = $employeeId !== null;

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $action = $_POST["action"] ?? "";

    if ($action === "login") {
        $username = clean_input($_POST["username"] ?? "");
        $password = $_POST["password"] ?? "";

        $employee = find_employee_by_username($username);
        if (!$employee || !password_verify($password, $employee["passwordHash"])) {
            $errors[] = "Invalid username or password.";
        } else {
            $_SESSION["employee_id"] = $employee["employeeID"];
            $_SESSION["employee_username"] = $employee["username"];
            $_SESSION["employee_role"] = $employee["role"];
            $isAuth = true;
            $employeeUsername = $employee["username"];
            $employeeRole = $employee["role"];
            $success = "Login successful!";
        }
    }

    if ($action === "logout") {
        session_destroy();
        header("Location: employee.php");
        exit;
    }

    if ($isAuth && $action === "add_package") {
        $trackCode = clean_input($_POST["trackCode"] ?? "");
        $phoneNumber = clean_input($_POST["phoneNumber"] ?? "");
        $shelf = clean_input($_POST["shelf"] ?? "");
        $price = (float)($_POST["price"] ?? 0);

        if (!validate_track_code($trackCode) || !validate_phone($phoneNumber) || $shelf === "" || $price < 0) {
            $errors[] = "Please fill package fields correctly.";
        } elseif (add_package($trackCode, $phoneNumber, $shelf, $price, $employeeUsername ?? "")) {
            $success = "Илгээмж амжилттай хадгалагдлаа.";
        } else {
            $errors[] = "Package insert failed.";
        }
    }

    if ($isAuth && $action === "mark_picked") {
        $packageId = (int)($_POST["packageID"] ?? 0);
        if ($packageId > 0 && mark_picked($packageId)) {
            $success = "Илгээмжийг авагдсан гэж тэмдэглэлээ.";
        } else {
            $errors[] = "Mark picked failed.";
        }
    }

    if ($isAuth && $action === "query_packages") {
        $queryText = clean_input($_POST["queryText"] ?? "");
        if ($queryText !== "") {
            $queryRows = search_packages($queryText);
            if (!$queryRows) {
                $success = "Хайлтанд тохирох илгээмж олдсонгүй.";
            }
        }
    }

    if ($isAuth && $action === "top_packages") {
        $topSort = $_POST["topSort"] ?? "newest";
        $topShowDeleted = isset($_POST["showDeleted"]);
        $topRows = list_top_packages($topSort, $topShowDeleted);
    }
}
?>
<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ажилтны хэсэг</title>
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
        <p class="ok"><?= htmlspecialchars($success) ?></p>
    <?php endif; ?>

    <?php foreach ($errors as $error): ?>
        <p class="err"><?= htmlspecialchars($error) ?></p>
    <?php endforeach; ?>

    <?php if (!$isAuth): ?>
    <div id="login-div">
        <h1>Employee Login</h1>
        <fieldset>
            <form method="post" action="employee.php">
                <input type="hidden" name="action" value="login">

                <label for="username">Username</label>
                <input id="username" type="text" name="username" required>

                <label for="password">Password</label>
                <input id="password" type="password" name="password" required>

                <button type="submit">Login</button>
            </form>
        </fieldset>
    </div>
    <?php endif; ?>

    <?php if ($isAuth): ?>
    <div id="dashboard-div">
        <h1>Employee Dashboard (<?= htmlspecialchars($employeeRole ?? "") ?>)</h1>
        <form method="post" action="employee.php" style="margin-bottom: 20px;">
            <input type="hidden" name="action" value="logout">
            <button type="submit">Logout</button>
        </form>

    <fieldset>
        <legend>Add new cargo</legend>
        <form method="post" action="employee.php">
            <input type="hidden" name="action" value="add_package">

            <label for="trackCode">Track code</label>
            <input id="trackCode" type="text" name="trackCode" maxlength="40" required>

            <label for="phoneNumber">Утасны дугаар</label>
            <input id="phoneNumber" type="text" name="phoneNumber" required>

            <label for="price">Үнэ</label>
            <input id="price" type="number" name="price" min="0" step="0.01" required>

            <label for="shelf">Shelf</label>
            <input id="shelf" type="text" name="shelf" maxlength="50" required>

            <button type="submit">Save</button>
        </form>
    </fieldset>

    <fieldset>
        <legend>Query</legend>
        <form method="post" action="employee.php">
            <input type="hidden" name="action" value="query_packages">

            <label for="queryText">Track code or phone number</label>
            <input id="queryText" type="text" name="queryText" value="<?= htmlspecialchars($queryText) ?>" required>

            <button type="submit">Хайх</button>
        </form>

        <?php if ($queryRows): ?>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Трек</th>
                        <th>Утас</th>
                        <th>Үнэ</th>
                        <th>isDeleted</th>
                        <th>Үүсгэсэн хугацаа</th>
                        <th>Үйлдэл</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($queryRows as $row): ?>
                        <tr>
                            <td><?= $row["packageID"] ?></td>
                            <td><?= htmlspecialchars($row["trackCode"]) ?></td>
                            <td><?= htmlspecialchars($row["phoneNumber"]) ?></td>
                            <td><?= number_format($row["price"], 2) ?></td>
                            <td><?= htmlspecialchars((string)$row["isDeleted"]) ?></td>
                            <td><?= htmlspecialchars($row["createdAt"]) ?></td>
                            <td>
                                <?php if ($row["isDeleted"] == 0): ?>
                                    <form method="post" action="employee.php" class="inline-form">
                                        <input type="hidden" name="action" value="mark_picked">
                                        <input type="hidden" name="packageID" value="<?= $row["packageID"] ?>">
                                        <button type="submit">delete</button>
                                    </form>
                                <?php else: ?>
                                    <span class="muted">deleted</span>
                                <?php endif; ?>
                            </td>
                        </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        <?php endif; ?>
    </fieldset>

    <fieldset>
        <legend>Top 50</legend>
        <form method="post" action="employee.php">
            <input type="hidden" name="action" value="top_packages">

            <label for="topSort">Sort</label>
            <select id="topSort" name="topSort">
                <option value="price" <?= $topSort === "price" ? "selected" : "" ?>>Price</option>
                <option value="oldest" <?= $topSort === "oldest" ? "selected" : "" ?>>Oldest</option>
                <option value="newest" <?= $topSort === "newest" ? "selected" : "" ?>>Newest</option>
            </select>

            <label style="display: inline-flex; align-items: center; gap: 8px; margin-top: 10px;">
                <input type="checkbox" name="showDeleted" value="1" <?= $topShowDeleted ? "checked" : "" ?>>
                isDeleted = 1
            </label>

            <button type="submit">Load</button>
        </form>

        <?php if ($topRows): ?>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Трек</th>
                        <th>Утас</th>
                        <th>Тавиур</th>
                        <th>Үнэ</th>
                        <th>isDeleted</th>
                        <th>Үүсгэсэн хугацаа</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($topRows as $row): ?>
                        <tr>
                            <td><?= $row["packageID"] ?></td>
                            <td><?= htmlspecialchars($row["trackCode"]) ?></td>
                            <td><?= htmlspecialchars($row["phoneNumber"]) ?></td>
                            <td><?= htmlspecialchars($row["shelf"]) ?></td>
                            <td><?= number_format($row["price"], 2) ?></td>
                            <td><?= htmlspecialchars((string)$row["isDeleted"]) ?></td>
                            <td><?= htmlspecialchars($row["createdAt"]) ?></td>
                        </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        <?php endif; ?>
    </fieldset>
    </div>
    <?php endif; ?>
</body>
</html>
