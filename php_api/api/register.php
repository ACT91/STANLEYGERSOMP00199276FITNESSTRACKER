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

if (!$input) {
    http_response_code(400);
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid JSON input'
    ]);
    exit();
}

// Validate required fields
$requiredFields = ['email', 'password', 'name', 'age'];
foreach ($requiredFields as $field) {
    if (!isset($input[$field]) || empty($input[$field])) {
        http_response_code(400);
        echo json_encode([
            'status' => 'error',
            'message' => "Missing required field: $field"
        ]);
        exit();
    }
}

// Sanitize input
$email = sanitizeInput($input['email']);
$password = $input['password'];
$name = sanitizeInput($input['name']);
$age = intval($input['age']);
$weight = isset($input['weight']) && !empty($input['weight']) ? floatval($input['weight']) : null;
$height = isset($input['height']) && !empty($input['height']) ? floatval($input['height']) : null;

// Validate email
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    http_response_code(400);
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid email format'
    ]);
    exit();
}

// Validate password length
if (strlen($password) < 6) {
    http_response_code(400);
    echo json_encode([
        'status' => 'error',
        'message' => 'Password must be at least 6 characters'
    ]);
    exit();
}

$conn = getDBConnection();

// Check if email already exists
$stmt = $conn->prepare("SELECT id FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    http_response_code(409);
    echo json_encode([
        'status' => 'error',
        'message' => 'Email already registered'
    ]);
    $stmt->close();
    $conn->close();
    exit();
}
$stmt->close();

// Hash password
$passwordHash = password_hash($password, PASSWORD_BCRYPT);

// Insert new user
$stmt = $conn->prepare("INSERT INTO users (email, password_hash, name, age, weight, height) VALUES (?, ?, ?, ?, ?, ?)");
$stmt->bind_param("sssidd", $email, $passwordHash, $name, $age, $weight, $height);

if ($stmt->execute()) {
    $userId = $conn->insert_id;

    http_response_code(201);
    echo json_encode([
        'status' => 'success',
        'message' => 'User registered successfully',
        'data' => [
            'id' => $userId,
            'email' => $email,
            'name' => $name,
            'age' => $age,
            'weight' => $weight,
            'height' => $height
        ]
    ]);
} else {
    http_response_code(500);
    echo json_encode([
        'status' => 'error',
        'message' => 'Registration failed: ' . $stmt->error
    ]);
}

$stmt->close();
$conn->close();
?>

