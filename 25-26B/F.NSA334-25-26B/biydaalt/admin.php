<?php
require_once __DIR__ . "/db.php";

$conn = db();
$errors = [];
$success = "";
$queryText = "";
$queryRows = [];
$recentPackages = [];

function statusMn($status)
{
    if ($status === "Arrived") {
        return "Ирсэн";
    }
    if ($status === "Pending") {
        return "Хүлээгдэж байгаа";
    }
    return $status;
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $action = (string)($_POST["action"] ?? "");

    if ($action === "add_package") {
        $trackCode = trim(strtoupper((string)($_POST["trackCode"] ?? "")));
        $phoneNumber = trim((string)($_POST["phoneNumber"] ?? ""));
        $shelf = trim((string)($_POST["shelf"] ?? ""));
        $priceText = trim((string)($_POST["price"] ?? ""));
        $price = is_numeric($priceText) ? (float)$priceText : 0.0;

        if ($trackCode === "" || $phoneNumber === "" || $shelf === "" || $priceText === "") {
            $errors[] = "Бүх талбарыг бөглөнө үү.";
        } else {
            $existsResult = $conn->query(
                "SELECT packageID FROM packages WHERE trackCode = '" . $trackCode . "' LIMIT 1"
            );

            if ($existsResult === false) {
                $errors[] = $conn->error;
            } elseif ($existsResult->fetch_assoc()) {
                $errors[] = "Энэ track code бүртгэгдсэн байна.";
            } else {
                $insertResult = $conn->query(
                    "INSERT INTO packages (
                        trackCode, phoneNumber, shelf, createdBy, status, price, arrivedAt, createdAt, isDeleted
                    ) VALUES (
                        '" . $trackCode . "',
                        '" . $phoneNumber . "',
                        '" . $shelf . "',
                        NULL,
                        'Arrived',
                        " . $price . ",
                        NOW(),
                        NOW(),
                        0
                    )"
                );
                if ($insertResult === false) {
                    $errors[] = $conn->error;
                } else {
                    $success = "Илгээмж амжилттай хадгалагдлаа.";
                }
            }
        }
        
    } elseif ($action === "mark_picked") {
        $packageId = (int)($_POST["packageID"] ?? 0);

        if ($packageId > 0) {
            $updateResult = $conn->query(
                "UPDATE packages SET isDeleted = 1 WHERE packageID = " . $packageId . " AND isDeleted = 0"
            );
            if ($updateResult === false) {
                $errors[] = $conn->error;
            } else {
                $success = "Илгээмжийг авагдсан гэж тэмдэглэлээ.";
            }
        } else {
            $errors[] = "Буруу package ID.";
        }
        
    } elseif ($action === "pickup_by_phone") {
        $phone = trim((string)($_POST["pickupPhone"] ?? ""));

        if ($phone === "") {
            $errors[] = "Утасны дугаар заавал оруулна.";
        } else {
            $updateResult = $conn->query(
                "UPDATE packages SET isDeleted = 1 WHERE phoneNumber = '" . $phone . "' AND isDeleted = 0"
            );
            if ($updateResult === false) {
                $errors[] = $conn->error;
            } else {
                $success = "Нийт " . $conn->affected_rows . " илгээмжийг авагдсан (isDeleted=1) болголоо.";
            }
        }
        
    } elseif ($action === "query_packages") {
        $queryText = trim((string)($_POST["queryText"] ?? ""));

        if ($queryText === "") {
            $errors[] = "Хайх утгаа оруулна уу.";
        } else {
            $queryRows = [];
            $result = $conn->query(
                "SELECT packageID, trackCode, phoneNumber, price, status, isDeleted, COALESCE(arrivedAt, createdAt) AS arrivedTime
                 FROM packages
                 WHERE trackCode LIKE '%" . $queryText . "%' OR phoneNumber LIKE '%" . $queryText . "%'
                 ORDER BY createdAt DESC
                 LIMIT 100"
            );

            if ($result === false) {
                $errors[] = $conn->error;
            } elseif ($result instanceof mysqli_result) {
                while ($row = $result->fetch_assoc()) {
                    $queryRows[] = $row;
                }
            }

            if (!$queryRows) {
                $success = "Хайлтанд тохирох илгээмж олдсонгүй.";
            }
        }
    }
}

$result = $conn->query(
    "SELECT packageID, trackCode, phoneNumber, price, status, isDeleted, COALESCE(arrivedAt, createdAt) AS arrivedTime
     FROM packages
     ORDER BY createdAt DESC
     LIMIT 50"
);

if ($result === false) {
    $errors[] = $conn->error;
} elseif ($result instanceof mysqli_result) {
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
    <h1>Admin Dashboard</h1>

    <?php if ($success !== ""): ?>
        <p class="ok"><?= $success ?></p>
    <?php endif; ?>

    <?php foreach ($errors as $error): ?>
        <p class="err"><?= $error ?></p>
    <?php endforeach; ?>

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
                            <td><?= $row["trackCode"] ?></td>
                            <td><?= $row["phoneNumber"] ?></td>
                            <td><?= number_format((float)$row["price"], 2) ?></td>
                            <td><?= statusMn((string)$row["status"]) ?></td>
                            <td><?= (int)$row["isDeleted"] ?></td>
                            <td><?= $row["arrivedTime"] ?></td>
                            <td>
                                <?php if ((int)$row["isDeleted"] === 0): ?>
                                    <form method="post" action="admin.php" class="inline-form">
                                        <input type="hidden" name="action" value="mark_picked">
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
                            <td><?= $package["trackCode"] ?></td>
                            <td><?= $package["phoneNumber"] ?></td>
                            <td><?= number_format((float)$package["price"], 2) ?></td>
                            <td><?= statusMn((string)$package["status"]) ?></td>
                            <td><?= (int)$package["isDeleted"] ?></td>
                            <td><?= $package["arrivedTime"] ?></td>
                            <td>
                                <?php if ((int)$package["isDeleted"] === 0): ?>
                                    <form method="post" action="admin.php" class="inline-form">
                                        <input type="hidden" name="action" value="mark_picked">
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
</body>
</html>
