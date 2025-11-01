<?php
require_once '../config.php';

setHeaders();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode([
        'status' => 'error',
        'message' => 'Method not allowed'
    ]);
    exit();
}

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

if (!$input || !isset($input['email']) || !isset($input['password'])) {
    http_response_code(400);
    echo json_encode([
        'status' => 'error',
        'message' => 'Email and password are required'
    ]);
    exit();
}

$email = sanitizeInput($input['email']);
$password = $input['password'];

$conn = getDBConnection();

// Get user by email
$stmt = $conn->prepare("SELECT id, email, password_hash, name, age, weight, height FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    http_response_code(401);
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid email or password'
    ]);
    $stmt->close();
    $conn->close();
    exit();
}

$user = $result->fetch_assoc();
$stmt->close();

// Verify password
if (!password_verify($password, $user['password_hash'])) {
    http_response_code(401);
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid email or password'
    ]);
    $conn->close();
    exit();
}

// Generate JWT token
$token = generateToken($user['id'], $user['email']);

http_response_code(200);
echo json_encode([
    'status' => 'success',
    'message' => 'Login successful',
    'token' => $token,
    'data' => [
        'id' => $user['id'],
        'email' => $user['email'],
        'name' => $user['name'],
        'age' => $user['age'],
        'weight' => $user['weight'],
        'height' => $user['height']
    ]
]);

$conn->close();
?>

