<?php
session_start();
require_once __DIR__ . "/includes/validation.php";
require_once __DIR__ . "/includes/customer.php";

$errors = [];
$success = "";

$phoneNumber = "";
$email = "";
$username = "";
$password = "";

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $phoneNumber = clean_input($_POST["phoneNumber"] ?? "");
    $email = clean_input($_POST["email"] ?? "");
    $username = clean_input($_POST["username"] ?? "");
    $password = $_POST["password"] ?? "";

    if (!validate_phone($phoneNumber)) {
        $errors[] = "Утасны дугаар 8 оронтой байх ёстой.";
    }
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $errors[] = "Имэйл буруу байна.";
    }
    if (!validate_username($username)) {
        $errors[] = "Username нь 3-20 тэмдэгт (үсэг/тоо/_) байна.";
    }
    if (!validate_password($password)) {
        $errors[] = "Нууц үг хамгийн багадаа 6 тэмдэгт байна.";
    }

    if (!$errors) {
        $passwordHash = password_hash($password, PASSWORD_DEFAULT);
        if (create_customer($username, $email, $phoneNumber, $passwordHash)) {
            $success = "Бүртгэл амжилттай. Нэвтэрч орно уу.";
            $phoneNumber = "";
            $email = "";
            $username = "";
        } else {
            $errors[] = "Бүртгэл амжилтгүй. Username эсвэл утас давхцаж байж магадгүй.";
        }
    }
}
?>
<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Хэрэглэгч бүртгэл</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 700px; margin: 30px auto; padding: 0 12px; }
        label { display: block; margin: 8px 0 4px; font-weight: 600; }
        input, button { width: 100%; padding: 8px; box-sizing: border-box; }
        button { cursor: pointer; margin-top: 10px; }
        .ok { color: #0b7a29; margin: 10px 0; }
        .err { color: #b00020; margin: 6px 0; }
    </style>
</head>
<body>
    <h1>Хэрэглэгч бүртгэл</h1>
    <p><a href="index.php">Нүүр хуудас</a> | <a href="login.php">Нэвтрэх</a></p>

    <?php if ($success !== ""): ?>
        <p class="ok"><?= htmlspecialchars($success) ?></p>
    <?php endif; ?>

    <?php foreach ($errors as $error): ?>
        <p class="err"><?= htmlspecialchars($error) ?></p>
    <?php endforeach; ?>

    <form method="post" action="signup.php">
        <label for="phoneNumber">Утасны дугаар (8 оронтой)</label>
        <input id="phoneNumber" type="text" name="phoneNumber" value="<?= htmlspecialchars($phoneNumber) ?>" required>

        <label for="email">Имэйл</label>
        <input id="email" type="email" name="email" value="<?= htmlspecialchars($email) ?>" required>

        <label for="username">Username</label>
        <input id="username" type="text" name="username" value="<?= htmlspecialchars($username) ?>" required>

        <label for="password">Нууц үг</label>
        <input id="password" type="password" name="password" required>

        <button type="submit">Бүртгүүлэх</button>
    </form>
</body>
</html>
