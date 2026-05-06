<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

session_start();
$auth = $_SESSION["auth"] ?? false;

$conn = new mysqli("127.0.0.1", "guest", "pass123", "biydaalt");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$errors = [];
$success = "";
$queryText = "";
$queryRows = [];
$recentPackages = [];
$sqlOutput = "";
$sqlError = "";

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $action = $_POST["action"] ?? "";

    if ($action === "login") {
        $username = $_POST["username"] ?? "";
        $password = $_POST["password"] ?? "";
        
        if ($username === "admin" && $password === "admin") {
            $_SESSION["auth"] = true;
            $auth = true;
            $success = "Login successful!";
        } else {
            $errors[] = "Invalid username or password";
        }
    } elseif ($auth && $action === "add_package") {
        $trackCode = $_POST["trackCode"] ?? "";
        $phoneNumber = $_POST["phoneNumber"] ?? "";
        $shelf = $_POST["shelf"] ?? "";
        $price = $_POST["price"] ?? 0;

        $conn->query(
            "INSERT INTO packages (trackCode, phoneNumber, shelf, price, createdAt, isDeleted)
             VALUES ('$trackCode', '$phoneNumber', '$shelf', $price, NOW(), 0)"
        );
        $success = "Илгээмж амжилттай хадгалагдлаа.";
        
    } elseif ($auth && $action === "mark_picked") {
        $packageId = $_POST["packageID"] ?? 0;
        $conn->query("UPDATE packages SET isDeleted = 1 WHERE packageID = $packageId AND isDeleted = 0");
        $success = "Илгээмжийг авагдсан гэж тэмдэглэлээ.";
        
    } elseif ($auth && $action === "pickup_by_phone") {
        $phone = $_POST["pickupPhone"] ?? "";
        $conn->query("UPDATE packages SET isDeleted = 1 WHERE phoneNumber = '$phone' AND isDeleted = 0");
        $success = "Нийт " . $conn->affected_rows . " илгээмжийг авагдсан (isDeleted=1) болголоо.";
        
    } elseif ($auth && $action === "query_packages") {
        $queryText = $_POST["queryText"] ?? "";
        $result = $conn->query(
            "SELECT packageID, trackCode, phoneNumber, price, isDeleted, createdAt
             FROM packages
             WHERE trackCode LIKE '%$queryText%' OR phoneNumber LIKE '%$queryText%'
             ORDER BY createdAt DESC
             LIMIT 100"
        );
        if ($result === false) {
            $errors[] = "Query error: " . $conn->error;
        } else {
            while ($row = $result->fetch_assoc()) {
                $queryRows[] = $row;
            }
            if (!$queryRows) {
                $success = "Хайлтанд тохирох илгээмж олдсонгүй.";
            }
        }
    } elseif ($auth && $action === "sql_query") {
        $queryText = $_POST["queryText"] ?? "";
        try {
            $result = $conn->query($queryText);
            if ($result === false) {
                $sqlError = "SQL Error: " . $conn->error;
            } else {
                if ($result instanceof mysqli_result) {
                    $sqlOutput = "<table border='1' style='border-collapse: collapse;'>";
                    $fields = $result->fetch_fields();
                    $sqlOutput .= "<tr>";
                    foreach ($fields as $field) {
                        $sqlOutput .= "<th style='padding: 8px; background: #eee;'>" . $field->name . "</th>";
                    }
                    $sqlOutput .= "</tr>";
                    while ($row = $result->fetch_assoc()) {
                        $sqlOutput .= "<tr>";
                        foreach ($row as $value) {
                            $sqlOutput .= "<td style='padding: 8px;'>" . htmlspecialchars($value ?? 'NULL') . "</td>";
                        }
                        $sqlOutput .= "</tr>";
                    }
                    $sqlOutput .= "</table>";
                } else {
                    $sqlOutput = "Query successful. Affected rows: " . $conn->affected_rows;
                }
            }
        } catch (Exception $e) {
            $sqlError = "Error: " . $e->getMessage();
        }
    }
}
?>
<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Карго админ</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 980px; margin: 30px auto; padding: 0 12px; }
        fieldset { margin-bottom: 20px; border: 1px solid #d5d5d5; padding: 16px; }
        label { display: block; margin: 8px 0 4px; font-weight: 600; }
        input, button { width: 100%; padding: 8px; box-sizing: border-box; }
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
    <?php if (!$auth): ?>
    <div id="login-div">
        <h1>Admin Login</h1>
        <fieldset>
            <form method="post" action="admin.php">
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

    <!-- DASHBOARD DIV -->
    <?php if ($auth): ?>
    <div id="dashboard-div">
        <h1>Admin Dashboard</h1>

    <fieldset>
        <legend>Add new cargo</legend>
        <form method="post" action="admin.php">
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
        <legend>Cargo авах</legend>
        <form method="post" action="admin.php">
            <input type="hidden" name="action" value="pickup_by_phone">

            <label for="pickupPhone">Утасны дугаар</label>
            <input id="pickupPhone" type="text" name="pickupPhone" required>

            <button type="submit">isDeleted=1</button>
        </form>
    </fieldset>

    <fieldset>
        <legend>Query</legend>
        <form method="post" action="admin.php">
            <input type="hidden" name="action" value="query_packages">

            <label for="queryText">Track code or phone number</label>
            <input id="queryText" type="text" name="queryText" value="<?= $queryText ?>" required>

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
                            <td><?= $row["trackCode"] ?></td>
                            <td><?= $row["phoneNumber"] ?></td>
                            <td><?= number_format($row["price"], 2) ?></td>
                            <td><?= $row["isDeleted"] ?></td>
                            <td><?= $row["createdAt"] ?></td>
                            <td>
                                <?php if ($row["isDeleted"] == 0): ?>
                                    <form method="post" action="admin.php" class="inline-form">
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
    	<form method="post" action="admin.php">
    	            <input type="hidden" name="action" value="sql_query">
    	    	
    	            <label for="sql_query">query command</label>
    	            <input id="queryText" type="text" name="queryText" value="<?= $queryText ?>" required>
    	
    	            <button type="submit">Хайх</button>
    	        </form>

        <?php if ($sqlError !== ""): ?>
            <div style="margin-top: 16px; color: #b00020;"><?= $sqlError ?></div>
        <?php endif; ?>

        <?php if ($sqlOutput !== ""): ?>
            <div style="margin-top: 16px;"><?= $sqlOutput ?></div>
        <?php endif; ?>
    </fieldset>
    </div>
    <?php endif; ?>
</body>
</html>
