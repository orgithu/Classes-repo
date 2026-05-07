<?php
session_start();
require_once __DIR__ . "/includes/validation.php";
require_once __DIR__ . "/includes/customer.php";

$errors = [];
$username = "";

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $username = clean_input($_POST["username"] ?? "");
    $password = $_POST["password"] ?? "";

    if (!validate_username($username)) {
        $errors[] = "Username буруу байна.";
    } elseif (!validate_password($password)) {
        $errors[] = "Нууц үг буруу байна.";
    } else {
        $customer = find_customer_by_username($username);
        if (!$customer || !password_verify($password, $customer["passwordHash"])) {
            $errors[] = "Нэвтрэх нэр эсвэл нууц үг буруу байна.";
        } else {
            $_SESSION["customer_id"] = $customer["customerID"];
            $_SESSION["customer_username"] = $customer["username"];
            $_SESSION["customer_phone"] = $customer["phoneNumber"];
            header("Location: dashboard.php");
            exit;
        }
    }
}
?>
<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Нэвтрэх</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 700px; margin: 30px auto; padding: 0 12px; }
        label { display: block; margin: 8px 0 4px; font-weight: 600; }
        input, button { width: 100%; padding: 8px; box-sizing: border-box; }
        button { cursor: pointer; margin-top: 10px; }
        .err { color: #b00020; margin: 6px 0; }
    </style>
</head>
<body>
    <h1>Нэвтрэх</h1>
    <p><a href="index.php">Нүүр хуудас</a> | <a href="signup.php">Бүртгүүлэх</a></p>

    <?php foreach ($errors as $error): ?>
        <p class="err"><?= htmlspecialchars($error) ?></p>
    <?php endforeach; ?>

    <form method="post" action="login.php">
        <label for="username">Username</label>
        <input id="username" type="text" name="username" value="<?= htmlspecialchars($username) ?>" required>

        <label for="password">Нууц үг</label>
        <input id="password" type="password" name="password" required>

        <button type="submit">Нэвтрэх</button>
    </form>
</body>
</html>
