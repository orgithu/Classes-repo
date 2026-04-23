<?php
declare(strict_types=1);

require_once __DIR__ . "/db.php";
session_start();

$conn = db();
$errors = [];
$success = "";
$loginMessage = "";
$usernameValue = "";
$queryText = "";
$queryRows = [];
$authToken = trim((string)($_POST["authToken"] ?? $_GET["token"] ?? ""));
$authEmployee = is_array($_SESSION["authEmployee"] ?? null) ? $_SESSION["authEmployee"] : null;
$auth = $authEmployee !== null;

function statusMn(string $status): string
{
    if ($status === "Arrived") {
        return "Ирсэн";
    }
    if ($status === "Pending") {
        return "Хүлээгдэж байгаа";
    }
    return $status;
}

function cleanPhone(string $value): string
{
    return preg_replace('/\D+/', "", $value) ?? "";
}

function findEmployee(mysqli $conn, string $username, string $password): ?array
{
    if ($username === "" || $password === "") {
        return null;
    }

    $passwordHash = md5($password);
    $stmt = $conn->prepare(
        "SELECT employeeID, employeeName
         FROM employees
         WHERE employeeName = ? AND password = ?
         LIMIT 1" );
    $stmt->bind_param("ss", $username, $passwordHash);
    $stmt->execute();
    $result = $stmt->get_result();
    $employee = $result->fetch_assoc();
    $stmt->close();
    return $employee ?: null;
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $action = (string)($_POST["action"] ?? "");

    if ($action === "login") {
        $username = trim((string)($_POST["username"] ?? ""));
        $password = (string)($_POST["password"] ?? "");
        $usernameValue = $username;
        $employee = findEmployee($conn, $username, $password);
        if ($employee) {
            $_SESSION["authEmployee"] = $employee;
            $authEmployee = $employee;
            $auth = true;
            $loginMessage = "Амжилттай нэвтэрлээ.";
        } else {
            unset($_SESSION["authEmployee"]);
            $authEmployee = null;
            $auth = false;
            $errors[] = "Нэвтрэх нэр эсвэл нууц үг буруу байна.";
        }
    } elseif ($action === "logout") {
        unset($_SESSION["authEmployee"]);
        $authEmployee = null;
        $auth = false;
        $success = "Logged out";
    } elseif (!$auth) {
        $errors[] = "Not authorized";
    } else {
        if ($action === "add_package") {
            $trackCode = strtoupper(trim((string)($_POST["trackCode"] ?? "")));
            $phoneNumber = cleanPhone((string)($_POST["phoneNumber"] ?? ""));
            $shelf = trim((string)($_POST["shelf"] ?? ""));
            $status = (string)($_POST["status"] ?? "Pending");
            $priceRaw = trim((string)($_POST["price"] ?? "0"));
            $price = is_numeric($priceRaw) ? (float)$priceRaw : -1;
            $arrivedAt = $status === "Arrived" ? date("Y-m-d H:i:s") : null;

            if ($trackCode === "") {
                $errors[] = "Track code заавал оруулна.";
            }
            if ($phoneNumber === "") {
                $errors[] = "Утасны дугаар заавал оруулна.";
            }
            if ($price < 0) {
                $errors[] = "Үнэ 0 эсвэл түүнээс их байх ёстой.";
            }

            if (!$errors) {
                $existsStmt = $conn->prepare(
                    "SELECT packageID
                     FROM packages
                     WHERE trackCode = ?
                     LIMIT 1"
                );
                $existsStmt->bind_param("s", $trackCode);
                $existsStmt->execute();
                $alreadyExists = (bool)$existsStmt->get_result()->fetch_assoc();
                $existsStmt->close();

                if ($alreadyExists) {
                    $errors[] = "Энэ track code бүртгэгдсэн байна.";
                } else {
                    $createdBy = (int)$authEmployee["employeeID"];
                    $insertStmt = $conn->prepare(
                        "INSERT INTO packages (
                            trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), 0)"
                    );
                    $insertStmt->bind_param(
                        "sssisds",
                        $trackCode,
                        $phoneNumber,
                        $shelf,
                        $createdBy,
                        $status,
                        $price,
                        $arrivedAt
                    );
                    $insertStmt->execute();
                    $insertStmt->close();
                    $success = "Илгээмж амжилттай хадгалагдлаа.";
                }
            }
        }

        if ($action === "mark_picked") {
            $packageId = (int)($_POST["packageID"] ?? 0);
            if ($packageId <= 0) {
                $errors[] = "Буруу package ID.";
            } else {
                $stmt = $conn->prepare(
                    "UPDATE packages
                     SET isDeleted = 1
                     WHERE packageID = ? AND isDeleted = 0"
                );
                $stmt->bind_param("i", $packageId);
                $stmt->execute();
                $affected = $stmt->affected_rows;
                $stmt->close();

                if ($affected > 0) {
                    $success = "Илгээмжийг авагдсан гэж тэмдэглэлээ.";
                } else {
                    $errors[] = "Илгээмж олдсонгүй эсвэл өмнө нь авагдсан байна.";
                }
            }
        }

        if ($action === "pickup_by_phone") {
            $phone = cleanPhone((string)($_POST["pickupPhone"] ?? ""));
            if ($phone === "") {
                $errors[] = "Утасны дугаар заавал оруулна.";
            } else {
                $stmt = $conn->prepare(
                    "UPDATE packages
                     SET isDeleted = 1
                     WHERE phoneNumber = ? AND isDeleted = 0"
                );
                $stmt->bind_param("s", $phone);
                $stmt->execute();
                $affected = $stmt->affected_rows;
                $stmt->close();
                $success = "Нийт {$affected} илгээмжийг авагдсан (isDeleted=1) болголоо.";
            }
        }

        if ($action === "query_packages") {
            $queryText = trim((string)($_POST["queryText"] ?? ""));
            $trackLike = "%" . strtoupper($queryText) . "%";
            $phoneLike = "%" . cleanPhone($queryText) . "%";
            if ($queryText === "") {
                $errors[] = "Хайх утгаа оруулна уу.";
            } else {
                $stmt = $conn->prepare(
                    "SELECT packageID, trackCode, phoneNumber, price, status, isDeleted, COALESCE(arrivedAt, createdAt) AS arrivedTime
                     FROM packages
                     WHERE trackCode LIKE ? OR phoneNumber LIKE ?
                     ORDER BY createdAt DESC
                     LIMIT 100"
                );
                $stmt->bind_param("ss", $trackLike, $phoneLike);
                $stmt->execute();
                $result = $stmt->get_result();
                while ($row = $result->fetch_assoc()) {
                    $queryRows[] = $row;
                }
                $stmt->close();
                if (!$queryRows) {
                    $success = "Хайлтанд тохирох илгээмж олдсонгүй.";
                }
            }
        }
    }
}

$recentPackages = [];
if ($authEmployee) {
    $result = $conn->query(
        "SELECT packageID, trackCode, phoneNumber, price, status, isDeleted, COALESCE(arrivedAt, createdAt) AS arrivedTime
         FROM packages
         ORDER BY createdAt DESC
         LIMIT 50"
    );
    while ($row = $result->fetch_assoc()) {
        $recentPackages[] = $row;
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
        input, select, button { width: 100%; padding: 8px; box-sizing: border-box; }
        button { cursor: pointer; margin-top: 10px; }
        .ok { color: #0b7a29; margin: 10px 0; }
        .err { color: #b00020; margin: 6px 0; }
        table { width: 100%; border-collapse: collapse; margin-top: 14px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        nav a { margin-right: 10px; }
        .inline-form { margin: 0; }
        .inline-form button { width: auto; padding: 6px 10px; margin: 0; }
        .muted { color: #666; font-size: 0.9rem; }
    </style>
</head>
<body>
    <h1>Карго компанийн админ</h1>
    <nav>
        <a href="index.php">Хэрэглэгчийн илгээмж хайх хуудас</a>
        <?php if ($authToken !== ""): ?>
            <a href="admin.php?token=<?= h($authToken) ?>">Админ хуудсыг шинэчлэх</a>
        <?php endif; ?>
    </nav>

    <?php if ($success !== ""): ?>
        <p class="ok"><?= h($success) ?></p>
    <?php endif; ?>

    <?php if ($loginMessage !== ""): ?>
        <p class="ok"><?= h($loginMessage) ?></p>
    <?php endif; ?>

    <?php foreach ($errors as $error): ?>
        <p class="err"><?= h($error) ?></p>
    <?php endforeach; ?>

    <?php if (!$auth): ?>
        <fieldset>
            <legend>Ажилтны нэвтрэх хэсэг</legend>
            <form method="post" action="admin.php">
                <input type="hidden" name="action" value="login">
                <label for="username">Ажилтны нэр</label>
                <input id="username" type="text" name="username" required value="<?= h($usernameValue) ?>">

                <label for="password">Нууц үг</label>
                <input id="password" type="password" name="password" required>

                <button type="submit">Нэвтрэх</button>
            </form>
        </fieldset>
    <?php else: ?>
        <fieldset>
            <form method="post" action="admin.php">
                <input type="hidden" name="action" value="logout">
                <button type="submit">Logout</button>
            </form>
        </fieldset>

        <fieldset>
            <legend>Add new cargo</legend>
            <form method="post" action="admin.php">
                <input type="hidden" name="action" value="add_package">
                <input type="hidden" name="authToken" value="<?= h($authToken) ?>">

                <label for="trackCode">Track code</label>
                <input id="trackCode" type="text" name="trackCode" maxlength="40" required>

                <label for="phoneNumber">Утасны дугаар</label>
                <input id="phoneNumber" type="text" name="phoneNumber" required>

                <label for="price">Үнэ</label>
                <input id="price" type="number" name="price" min="0" step="0.01" required>

                <label for="shelf">Shelf</label>
                <input id="shelf" type="text" name="shelf" maxlength="50" required>

                <label for="status">Төлөв</label>
                <select id="status" name="status">
                    <option value="Arrived">Ирсэн</option>
                </select>

                <button type="submit">Save</button>
            </form>
        </fieldset>

        <fieldset>
            <legend>Cargo авах</legend>
            <form method="post" action="admin.php">
                <input type="hidden" name="action" value="pickup_by_phone">
                <input type="hidden" name="authToken" value="<?= h($authToken) ?>">

                <label for="pickupPhone">Утасны дугаар</label>
                <input id="pickupPhone" type="text" name="pickupPhone" required>

                <button type="submit">isDeleted=1</button>
            </form>
        </fieldset>

        <fieldset>
            <legend>Query</legend>
            <form method="post" action="admin.php">
                <input type="hidden" name="action" value="query_packages">
                <input type="hidden" name="authToken" value="<?= h($authToken) ?>">

                <label for="queryText">Track code or phone number</label>
                <input id="queryText" type="text" name="queryText" value="<?= h($queryText) ?>" required>

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
                            <th>Төлөв</th>
                            <th>isDeleted</th>
                            <th>Хугацаа</th>
                            <th>Үйлдэл</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($queryRows as $row): ?>
                            <tr>
                                <td><?= (int)$row["packageID"] ?></td>
                                <td><?= h((string)$row["trackCode"]) ?></td>
                                <td><?= h((string)$row["phoneNumber"]) ?></td>
                                <td><?= number_format((float)$row["price"], 2) ?></td>
                                <td><?= h(statusMn((string)$row["status"])) ?></td>
                                <td><?= (int)$row["isDeleted"] ?></td>
                                <td><?= h((string)$row["arrivedTime"]) ?></td>
                                <td>
                                    <?php if ((int)$row["isDeleted"] === 0): ?>
                                        <form method="post" action="admin.php" class="inline-form">
                                            <input type="hidden" name="action" value="mark_picked">
                                            <input type="hidden" name="authToken" value="<?= h($authToken) ?>">
                                            <input type="hidden" name="packageID" value="<?= (int)$row["packageID"] ?>">
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
            <legend>Сүүлийн 50 илгээмж</legend>
            <?php if (!$recentPackages): ?>
                <p>Илгээмж алга.</p>
            <?php else: ?>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Трек</th>
                            <th>Утас</th>
                            <th>Үнэ</th>
                            <th>Төлөв</th>
                            <th>isDeleted</th>
                            <th>Ирсэн/Үүсгэсэн хугацаа</th>
                            <th>Үйлдэл</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($recentPackages as $package): ?>
                            <tr>
                                <td><?= (int)$package["packageID"] ?></td>
                                <td><?= h((string)$package["trackCode"]) ?></td>
                                <td><?= h((string)$package["phoneNumber"]) ?></td>
                                <td><?= number_format((float)$package["price"], 2) ?></td>
                                <td><?= h(statusMn((string)$package["status"])) ?></td>
                                <td><?= (int)$package["isDeleted"] ?></td>
                                <td><?= h((string)$package["arrivedTime"]) ?></td>
                                <td>
                                    <?php if ((int)$package["isDeleted"] === 0): ?>
                                        <form method="post" action="admin.php" class="inline-form">
                                            <input type="hidden" name="action" value="mark_picked">
                                            <input type="hidden" name="authToken" value="<?= h($authToken) ?>">
                                            <input type="hidden" name="packageID" value="<?= (int)$package["packageID"] ?>">
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
    <?php endif; ?>
</body>
</html>
