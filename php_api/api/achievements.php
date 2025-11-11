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
        handleGetAchievements($conn, $userData);
        break;
    default:
        http_response_code(405);
        echo json_encode([
            'status' => 'error',
            'message' => 'Method not allowed'
        ]);
}

$conn->close();

// Get user achievements
function handleGetAchievements($conn, $userData) {
    if (!isset($_GET['user_id'])) {
        http_response_code(400);
        echo json_encode([
            'status' => 'error',
            'message' => 'user_id parameter is required'
        ]);
        return;
    }

    $userId = intval($_GET['user_id']);

    // Verify user can only access their own achievements
    if ($userId !== $userData['user_id']) {
        http_response_code(403);
        echo json_encode([
            'status' => 'error',
            'message' => 'Forbidden - You can only access your own achievements'
        ]);
        return;
    }

    $stmt = $conn->prepare("SELECT * FROM achievements WHERE user_id = ? ORDER BY achieved_at DESC");
    $stmt->bind_param("i", $userId);
    $stmt->execute();
    $result = $stmt->get_result();

    $achievements = [];
    while ($row = $result->fetch_assoc()) {
        $achievements[] = [
            'id' => $row['id'],
            'userId' => $row['user_id'],
            'goalId' => $row['goal_id'],
            'title' => $row['title'],
            'description' => $row['description'],
            'achievedAt' => $row['achieved_at'],
            'unlocked' => true
        ];
    }

    $stmt->close();

    http_response_code(200);
    echo json_encode([
        'status' => 'success',
        'data' => $achievements
    ]);
}
?>

