<?php
$conn = new mysqli(
    getenv('DB_HOST'),
    getenv('DB_USER'),
    getenv('DB_PASS'),
    getenv('DB_NAME')
);

if ($conn->connect_error) {
        die("DB connection failed");
    }
if (isset($_GET['run'])) {
    $conn = new mysqli($host, $user, $pass, $db);

    $sql = "SELECT * FROM students;";
    $result = $conn->query($sql);
    
    if (!$result) {
        die("Query failed: " . $conn->error);
    }
    $rows = $result->fetch_all(MYSQLI_ASSOC);
    echo "<pre>";
    print_r($rows);
    echo "</pre>";
    
    $conn->close();
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Simple MySQL Trigger</title>
</head>
<body>

<form method="GET">
    <button type="submit" name="run" value="1">Run MySQL Query</button>
</form>

</body>
</html>
