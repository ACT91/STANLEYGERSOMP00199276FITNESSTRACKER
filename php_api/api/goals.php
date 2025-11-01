<?php
require_once '../config.php';

setHeaders();

// Verify authentication
$token = getAuthToken();
$userData = verifyToken($token);

if (!$userData) {
    http_response_code(401);
    echo json_encode([
        'status' => 'error',
        'message' => 'Unauthorized - Invalid or expired token'
    ]);
    exit();
}

$conn = getDBConnection();

// Handle different HTTP methods
switch ($_SERVER['REQUEST_METHOD']) {
    case 'GET':
        handleGetGoals($conn, $userData);
        break;
    case 'POST':
        handleCreateGoal($conn, $userData);
        break;
    case 'PUT':
        handleUpdateGoal($conn, $userData);
        break;
    default:
        http_response_code(405);
        echo json_encode([
            'status' => 'error',
            'message' => 'Method not allowed'
        ]);
}

$conn->close();

// Get user goals
function handleGetGoals($conn, $userData) {
    if (!isset($_GET['user_id'])) {
        http_response_code(400);
        echo json_encode([
            'status' => 'error',
            'message' => 'user_id parameter is required'
        ]);
        return;
    }

    $userId = intval($_GET['user_id']);

    // Verify user can only access their own goals
    if ($userId !== $userData['user_id']) {
        http_response_code(403);
        echo json_encode([
            'status' => 'error',
            'message' => 'Forbidden - You can only access your own goals'
        ]);
        return;
    }

    $stmt = $conn->prepare("SELECT * FROM fitness_goals WHERE user_id = ? ORDER BY deadline ASC");
    $stmt->bind_param("i", $userId);
    $stmt->execute();
    $result = $stmt->get_result();

    $goals = [];
    while ($row = $result->fetch_assoc()) {
        $goals[] = [
            'id' => $row['id'],
            'userId' => $row['user_id'],
            'goalType' => $row['goal_type'],
            'targetValue' => $row['target_value'],
            'currentValue' => $row['current_value'],
            'deadline' => $row['deadline'],
            'achieved' => (bool)$row['achieved'],
            'createdAt' => $row['created_at']
        ];
    }

    $stmt->close();

    http_response_code(200);
    echo json_encode([
        'status' => 'success',
        'data' => $goals
    ]);
}

// Create new goal
function handleCreateGoal($conn, $userData) {
    $input = json_decode(file_get_contents('php://input'), true);

    if (!$input) {
        http_response_code(400);
        echo json_encode([
            'status' => 'error',
            'message' => 'Invalid JSON input'
        ]);
        return;
    }

    $userId = intval($input['userId']);
    $goalType = sanitizeInput($input['goalType']);
    $targetValue = floatval($input['targetValue']);
    $currentValue = isset($input['currentValue']) ? floatval($input['currentValue']) : 0;
    $deadline = sanitizeInput($input['deadline']);

    // Verify user can only create goals for themselves
    if ($userId !== $userData['user_id']) {
        http_response_code(403);
        echo json_encode([
            'status' => 'error',
            'message' => 'Forbidden'
        ]);
        return;
    }

    $stmt = $conn->prepare("INSERT INTO fitness_goals (user_id, goal_type, target_value, current_value, deadline) VALUES (?, ?, ?, ?, ?)");
    $stmt->bind_param("isdds", $userId, $goalType, $targetValue, $currentValue, $deadline);

    if ($stmt->execute()) {
        $goalId = $conn->insert_id;

        http_response_code(201);
        echo json_encode([
            'status' => 'success',
            'message' => 'Goal created successfully',
            'data' => [
                'id' => $goalId,
                'userId' => $userId,
                'goalType' => $goalType,
                'targetValue' => $targetValue,
                'currentValue' => $currentValue,
                'deadline' => $deadline,
                'achieved' => false
            ]
        ]);
    } else {
        http_response_code(500);
        echo json_encode([
            'status' => 'error',
            'message' => 'Failed to create goal'
        ]);
    }

    $stmt->close();
}

// Update goal
function handleUpdateGoal($conn, $userData) {
    if (!isset($_GET['id'])) {
        http_response_code(400);
        echo json_encode([
            'status' => 'error',
            'message' => 'Goal id is required'
        ]);
        return;
    }

    $goalId = intval($_GET['id']);
    $input = json_decode(file_get_contents('php://input'), true);

    if (!$input) {
        http_response_code(400);
        echo json_encode([
            'status' => 'error',
            'message' => 'Invalid JSON input'
        ]);
        return;
    }

    // Verify goal belongs to user
    $stmt = $conn->prepare("SELECT user_id FROM fitness_goals WHERE id = ?");
    $stmt->bind_param("i", $goalId);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows === 0) {
        http_response_code(404);
        echo json_encode([
            'status' => 'error',
            'message' => 'Goal not found'
        ]);
        $stmt->close();
        return;
    }

    $goal = $result->fetch_assoc();
    $stmt->close();

    if ($goal['user_id'] !== $userData['user_id']) {
        http_response_code(403);
        echo json_encode([
            'status' => 'error',
            'message' => 'Forbidden'
        ]);
        return;
    }

    // Update goal
    $currentValue = floatval($input['currentValue']);
    $achieved = isset($input['achieved']) ? (bool)$input['achieved'] : false;

    $stmt = $conn->prepare("UPDATE fitness_goals SET current_value = ?, achieved = ? WHERE id = ?");
    $stmt->bind_param("dii", $currentValue, $achieved, $goalId);

    if ($stmt->execute()) {
        http_response_code(200);
        echo json_encode([
            'status' => 'success',
            'message' => 'Goal updated successfully'
        ]);
    } else {
        http_response_code(500);
        echo json_encode([
            'status' => 'error',
            'message' => 'Failed to update goal'
        ]);
    }

    $stmt->close();
}
?>

