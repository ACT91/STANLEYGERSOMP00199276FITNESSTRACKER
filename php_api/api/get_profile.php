<?php
require_once '../config.php';

setHeaders();

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Method not allowed']);
    exit();
}

$token = getAuthToken();
$userData = verifyToken($token);
if (!$userData) {
    http_response_code(401);
    echo json_encode(['status' => 'error', 'message' => 'Unauthorized']);
    exit();
}

if (!isset($_GET['user_id'])) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'user_id is required']);
    exit();
}

$userId = intval($_GET['user_id']);
if ($userId !== intval($userData['user_id'])) {
    http_response_code(403);
    echo json_encode(['status' => 'error', 'message' => 'Forbidden']);
    exit();
}

$conn = getDBConnection();
$stmt = $conn->prepare("SELECT id, email, name, age, weight, height, created_at FROM users WHERE id = ?");
$stmt->bind_param("i", $userId);
$stmt->execute();
$res = $stmt->get_result();

if ($row = $res->fetch_assoc()) {
    echo json_encode(['status' => 'success', 'data' => [
        'id' => intval($row['id']),
        'email' => $row['email'],
        'name' => $row['name'],
        'age' => intval($row['age']),
        'weight' => floatval($row['weight']),
        'height' => floatval($row['height']),
        'createdAt' => $row['created_at']
    ]]);
} else {
    http_response_code(404);
    echo json_encode(['status' => 'error', 'message' => 'User not found']);
}

$stmt->close();
$conn->close();

