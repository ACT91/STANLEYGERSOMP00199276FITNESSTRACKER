<?php
require_once '../config.php';

setHeaders();

if ($_SERVER['REQUEST_METHOD'] !== 'PUT' && $_SERVER['REQUEST_METHOD'] !== 'POST') {
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

$input = json_decode(file_get_contents('php://input'), true);
if (!$input) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Invalid JSON input']);
    exit();
}

$required = ['userId', 'name', 'age', 'weight', 'height'];
foreach ($required as $f) {
    if (!isset($input[$f])) {
        http_response_code(400);
        echo json_encode(['status' => 'error', 'message' => "Missing field: $f"]);
        exit();
    }
}

$userId = intval($input['userId']);
if ($userId !== intval($userData['user_id'])) {
    http_response_code(403);
    echo json_encode(['status' => 'error', 'message' => 'Forbidden']);
    exit();
}

$name = sanitizeInput($input['name']);
$age = intval($input['age']);
$weight = floatval($input['weight']);
$height = floatval($input['height']);

$currentPassword = isset($input['currentPassword']) ? $input['currentPassword'] : null;
$newPassword = isset($input['newPassword']) ? $input['newPassword'] : null;

$conn = getDBConnection();

// If password change requested, verify current password
if ($newPassword) {
    $stmt = $conn->prepare("SELECT password_hash FROM users WHERE id = ?");
    $stmt->bind_param("i", $userId);
    $stmt->execute();
    $res = $stmt->get_result();
    if ($row = $res->fetch_assoc()) {
        if (!$currentPassword || !password_verify($currentPassword, $row['password_hash'])) {
            http_response_code(400);
            echo json_encode(['status' => 'error', 'message' => 'Current password is incorrect']);
            $stmt->close();
            $conn->close();
            exit();
        }
    } else {
        http_response_code(404);
        echo json_encode(['status' => 'error', 'message' => 'User not found']);
        $stmt->close();
        $conn->close();
        exit();
    }
    $stmt->close();
}

if ($newPassword) {
    $passwordHash = password_hash($newPassword, PASSWORD_BCRYPT);
    $stmt = $conn->prepare("UPDATE users SET name = ?, age = ?, weight = ?, height = ?, password_hash = ? WHERE id = ?");
    // Types: s (name), i (age), d (weight), d (height), s (password), i (id)
    $stmt->bind_param("siddsi", $name, $age, $weight, $height, $passwordHash, $userId);
} else {
    $stmt = $conn->prepare("UPDATE users SET name = ?, age = ?, weight = ?, height = ? WHERE id = ?");
    // Types: s (name), i (age), d (weight), d (height), i (id)
    $stmt->bind_param("siddi", $name, $age, $weight, $height, $userId);
}

if (!$stmt->execute()) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Failed to update profile']);
    $stmt->close();
    $conn->close();
    exit();
}
$stmt->close();

// Return updated user
$stmt = $conn->prepare("SELECT id, email, name, age, weight, height, created_at FROM users WHERE id = ?");
$stmt->bind_param("i", $userId);
$stmt->execute();
$res = $stmt->get_result();
$row = $res->fetch_assoc();

http_response_code(200);

echo json_encode(['status' => 'success', 'message' => 'Profile updated', 'data' => [
    'id' => intval($row['id']),
    'email' => $row['email'],
    'name' => $row['name'],
    'age' => intval($row['age']),
    'weight' => floatval($row['weight']),
    'height' => floatval($row['height']),
    'createdAt' => $row['created_at']
]]);

$stmt->close();
$conn->close();

