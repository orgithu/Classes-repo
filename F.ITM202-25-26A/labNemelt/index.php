<?php
if (function_exists('opcache_reset')) { opcache_reset(); }

$servername = "localhost";
$username = "root";
$password = "secret";
$dbname = "dynamic_website";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$id = isset($_GET['id']) ? intval($_GET['id']) : 1;

$sql = "SELECT * FROM Page WHERE id = $id";
$result = $conn->query($sql);
$page = $result->fetch_assoc();

if (!$page) {
    $sql = "SELECT * FROM Page ORDER BY id LIMIT 1";
    $result = $conn->query($sql);
    $page = $result->fetch_assoc();
}

$sql_menu = "SELECT id, title FROM Page ORDER BY id";
$result_menu = $conn->query($sql_menu);
$menu_items = [];
while ($row = $result_menu->fetch_assoc()) {
    $menu_items[] = $row;
}

$conn->close();
?>

<!DOCTYPE html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo htmlspecialchars($page['title']); ?></title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        header { background-color: #333; color: white; padding: 1em; text-align: center; }
        nav { background-color: #f4f4f4; padding: 0.5em; }
        nav ul { list-style-type: none; padding: 0; text-align: center; }
        nav ul li { display: inline; margin: 0 1em; }
        nav ul li a { text-decoration: none; color: #333; }
        main { padding: 1em; }
        footer { background-color: #333; color: white; text-align: center; padding: 1em; position: fixed; bottom: 0; width: 100%; }
    </style>
</head>
<body>
    <header>
        <h1>dynamic_website</h1>
    </header>
    <nav>
        <ul>
            <?php foreach ($menu_items as $item): ?>
                <li><a href="index.php?id=<?php echo $item['id']; ?>"><?php echo htmlspecialchars($item['title']); ?></a></li>
            <?php endforeach; ?>
        </ul>
    </nav>
    <main>
        <h2><?php echo htmlspecialchars($page['title']); ?></h2>
        <p><?php echo nl2br(htmlspecialchars($page['content'])); ?></p>
    </main>
    <footer>
        <p>&copy; 2025 footer.</p>
    </footer>
</body>
</html>
