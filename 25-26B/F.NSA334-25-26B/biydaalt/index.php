<?php
$conn = new mysqli("127.0.0.1", "guest", "pass123", "biydaalt");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$query = $_GET["q"] ?? "";
$message = "";
$packages = [];
$totalPrice = 0.0;

if ($query !== "") {
    $result = $conn->query(
        "SELECT trackCode, phoneNumber, price, createdAt
         FROM packages
         WHERE isDeleted = 0
           AND (trackCode LIKE '%$query%' OR phoneNumber LIKE '%$query%')
         ORDER BY createdAt DESC"
    );
    
    while ($row = $result->fetch_assoc()) {
        $packages[] = $row;
    }

    if (!$packages) {
        $message = "Илгээмж олдсонгүй.";
    } else {
        foreach ($packages as $package) {
            $totalPrice += $package["price"];
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
    <p><a href="admin.php">Ажилтны админ хэсэг</a></p>

    <form method="get" action="index.php">
        <label for="q"><strong>Trackcode эсвэл Утасны дугаар</strong></label><br>
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
                    <div>Утас: <?= $package["phoneNumber"] ?></div>
                    <div>>Үнэ: <?= number_format((float)$package["price"], 2) ?></div>
                    <div>Үүсгэсэн хугацаа: <?= $package["createdAt"] ?></div>
                    <small>Трек: <?= $package["trackCode"] ?></small>
                </li>
            <?php endforeach; ?>
        </ol>
    <?php endif; ?>
</body>
</html>
