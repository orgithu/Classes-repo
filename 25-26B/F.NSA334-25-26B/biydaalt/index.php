<?php
declare(strict_types=1);

require_once __DIR__ . "/db.php";
$conn = db();

$query = trim((string)($_GET["q"] ?? ""));
$trackSearch = strtoupper($query);
$phoneSearch = preg_replace('/\D+/', "", $query);
$message = "";
$packages = [];
$totalPrice = 0.0;

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

if ($query !== "") {
    $stmt = $conn->prepare(
        "SELECT trackCode, phoneNumber, price, status, createdAt AS arrivedTime
         FROM packages
         WHERE isDeleted = 0
           AND (trackCode = ? OR phoneNumber = ?)
         ORDER BY createdAt DESC, packageID DESC"
    );
    $stmt->bind_param("ss", $trackSearch, $phoneSearch);
    $stmt->execute();
    $result = $stmt->get_result();
    while ($row = $result->fetch_assoc()) {
        $packages[] = $row;
    }
    $stmt->close();

    if (!$packages) {
        $message = "Илгээмж олдсонгүй.";
    } else {
        $arrivedCount = 0;
        foreach ($packages as $package) {
            $totalPrice += (float)$package["price"];
            if ((string)$package["status"] === "Arrived") {
                $arrivedCount++;
            }
        }

        if ($arrivedCount > 0) {
            $message = "{$arrivedCount} илгээмж ирсэн байна.";
        } else {
            $message = "Илгээмж ирээгүй";
        }
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
        <label for="q"><strong>Трек дугаар эсвэл утасны дугаар</strong></label><br>
        <input id="q" type="text" name="q" value="<?= h($query) ?>" placeholder="ж: TRK777 эсвэл 99110000">
        <button type="submit">Хайх</button>
    </form>

    <p class="msg"><?= h($message) ?></p>
    <p class="total">Жагсаалтад байгаа илгээмжийн нийт үнэ: <?= number_format($totalPrice, 2) ?></p>

    <?php if (!$packages): ?>
        <p>cargo гоо хайна уу.</p>
    <?php else: ?>
        <ol>
            <?php foreach ($packages as $package): ?>
                <li>
                    <div><strong>Утас:</strong> <?= h((string)$package["phoneNumber"]) ?></div>
                    <div><strong>Үнэ:</strong> <?= number_format((float)$package["price"], 2) ?></div>
                    <div><strong>Ирсэн хугацаа:</strong> <?= h((string)$package["arrivedTime"]) ?></div>
                    <div><strong>Төлөв:</strong> <?= h(statusMn((string)$package["status"])) ?></div>
                    <small>Трек: <?= h((string)$package["trackCode"]) ?></small>
                </li>
            <?php endforeach; ?>
        </ol>
    <?php endif; ?>
</body>
</html>
