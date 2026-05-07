<?php
require_once __DIR__ . "/includes/validation.php";
require_once __DIR__ . "/includes/package.php";

$query = $_GET["q"] ?? "";
$query = clean_input($query);
$message = "";
$packages = [];
$totalPrice = 0.0;

if ($query !== "") {
    $packages = public_search_packages($query);

    if (!$packages) {
        $message = "Илгээмж олдсонгүй.";
    } else {
        foreach ($packages as $package) {
            $totalPrice += (float)$package["price"];
        }
        $message = count($packages) . " илгээмж олдсон.";
    }
} else {
    $message = "Утасны дугаар эсвэл track code оруулна уу.";
}
?>
<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Карго илгээмж хайлт</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 900px; margin: 30px auto; padding: 0 12px; }
        form { margin-bottom: 14px; }
        input, button { padding: 8px; }
        input { width: 70%; max-width: 440px; }
        button { cursor: pointer; }
        .msg { margin: 10px 0; color: #1f2937; }
        .total { margin: 12px 0; font-weight: 700; }
        ol li { margin-bottom: 10px; }
        small { color: #666; }
    </style>
</head>
<body>
    <h1>Карго илгээмж хайлт</h1>
    <p>
        <a href="employee.php">Ажилтны хэсэг</a> |
        <a href="admin.php">DB Админ</a> |
        <a href="login.php">Нэвтрэх</a> |
        <a href="signup.php">Бүртгүүлэх</a>
    </p>

    <form method="get" action="index.php">
        <label for="q"><strong>Trackcode</strong></label><br>
        <input id="q" type="text" name="q" value="<?= $query ?>">
        <button type="submit">Хайх</button>
    </form>

    <p class="msg"><?= $message ?></p>
    <p class="total">нийт үнэ: <?= number_format($totalPrice, 2) ?></p>

    <?php if (!$packages): ?>
        <p>cargo гоо хайна уу.</p>
    <?php else: ?>
        <ol>
            <?php foreach ($packages as $package): ?>
                <li>
                    <div>Үнэ: <?= number_format((float)$package["price"], 2) ?></div>
                    <small>Трек: <?= htmlspecialchars($package["trackCode"]) ?></small>
                </li>
            <?php endforeach; ?>
        </ol>
    <?php endif; ?>
</body>
</html>
