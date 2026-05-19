<?php
session_start();
require_once __DIR__ . "/includes/auth.php";
require_once __DIR__ . "/includes/package.php";

require_customer();

$customerPhone = $_SESSION["customer_phone"] ?? "";
$packages = [];
if ($customerPhone !== "") {
    $packages = list_packages_by_phone($customerPhone);
}

if ($_SERVER["REQUEST_METHOD"] === "POST" && ($_POST["action"] ?? "") === "logout") {
    session_destroy();
    header("Location: index.php");
    exit;
}
?>
<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Миний илгээмжүүд</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 900px; margin: 30px auto; padding: 0 12px; }
        table { width: 100%; border-collapse: collapse; margin-top: 14px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        .muted { color: #666; }
        button { padding: 8px 12px; cursor: pointer; }
    </style>
</head>
<body>
    <h1>Миний илгээмжүүд</h1>
    <p><a href="index.php">Нүүр хуудас</a></p>
    <form method="post" action="dashboard.php">
        <input type="hidden" name="action" value="logout">
        <button type="submit">Гарах</button>
    </form>

    <?php if (!$packages): ?>
        <p class="muted">Илгээмж олдсонгүй.</p>
    <?php else: ?>
        <table>
            <thead>
                <tr>
                    <th>Трек</th>
                    <th>Утас</th>
                    <th>Тавиур</th>
                    <th>Үнэ</th>
                    <th>Төлөв</th>
                    <th>Үүсгэсэн</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($packages as $package): ?>
                    <tr>
                        <td><?= htmlspecialchars($package["trackCode"]) ?></td>
                        <td><?= htmlspecialchars($package["phoneNumber"]) ?></td>
                        <td><?= htmlspecialchars($package["shelf"]) ?></td>
                        <td><?= number_format((float)$package["price"], 2) ?></td>
                        <td><?= $package["isDeleted"] ? "Авсан" : "Хүлээгдэж буй" ?></td>
                        <td><?= htmlspecialchars($package["createdAt"]) ?></td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php endif; ?>
</body>
</html>
