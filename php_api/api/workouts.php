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
        handleGetWorkouts($conn, $userData);
        break;
    case 'POST':
        handleCreateWorkout($conn, $userData);
        break;
    case 'PUT':
        handleUpdateWorkout($conn, $userData);
        break;
    case 'DELETE':
        handleDeleteWorkout($conn, $userData);
        break;
    default:
        http_response_code(405);
        echo json_encode([
            'status' => 'error',
            'message' => 'Method not allowed'
        ]);
}

$conn->close();

// Get user workouts
function handleGetWorkouts($conn, $userData) {
    if (!isset($_GET['user_id'])) {
        http_response_code(400);
        echo json_encode([
            'status' => 'error',
            'message' => 'user_id parameter is required'
        ]);
        return;
    }

    $userId = intval($_GET['user_id']);

    // Verify user can only access their own workouts
    if ($userId !== $userData['user_id']) {
        http_response_code(403);
        echo json_encode([
            'status' => 'error',
            'message' => 'Forbidden - You can only access your own workouts'
        ]);
        return;
    }

    $stmt = $conn->prepare("SELECT * FROM workouts WHERE user_id = ? ORDER BY start_time DESC");
    $stmt->bind_param("i", $userId);
    $stmt->execute();
    $result = $stmt->get_result();

    $workouts = [];
    while ($row = $result->fetch_assoc()) {
        $workout = [
            'id' => $row['id'],
            'userId' => $row['user_id'],
            'workoutType' => $row['workout_type'],
            'startTime' => $row['start_time'],
            'endTime' => $row['end_time'],
            'duration' => $row['duration'],
            'caloriesBurned' => $row['calories_burned']
        ];

        // Get workout-specific details
        switch ($row['workout_type']) {
            case 'running':
                $detailStmt = $conn->prepare("SELECT * FROM running_workouts WHERE workout_id = ?");
                $detailStmt->bind_param("i", $row['id']);
                $detailStmt->execute();
                $detailResult = $detailStmt->get_result();
                if ($detail = $detailResult->fetch_assoc()) {
                    $workout['distance'] = $detail['distance'];
                    $workout['averagePace'] = $detail['average_pace'];
                    $workout['routeData'] = $detail['route_data'];
                }
                $detailStmt->close();
                break;

            case 'weightlifting':
                $detailStmt = $conn->prepare("SELECT * FROM weightlifting_workouts WHERE workout_id = ?");
                $detailStmt->bind_param("i", $row['id']);
                $detailStmt->execute();
                $detailResult = $detailStmt->get_result();
                if ($detail = $detailResult->fetch_assoc()) {
                    $workout['exerciseName'] = $detail['exercise_name'];
                    $workout['totalSets'] = $detail['total_sets'];
                    $workout['totalReps'] = $detail['total_reps'];
                    $workout['maxWeight'] = $detail['max_weight'];
                }
                $detailStmt->close();
                break;

            case 'cycling':
                $detailStmt = $conn->prepare("SELECT * FROM cycling_workouts WHERE workout_id = ?");
                $detailStmt->bind_param("i", $row['id']);
                $detailStmt->execute();
                $detailResult = $detailStmt->get_result();
                if ($detail = $detailResult->fetch_assoc()) {
                    $workout['distance'] = $detail['distance'];
                    $workout['averageSpeed'] = $detail['average_speed'];
                }
                $detailStmt->close();
                break;
        }

        $workouts[] = $workout;
    }

    $stmt->close();

    http_response_code(200);
    echo json_encode([
        'status' => 'success',
        'data' => $workouts
    ]);
}

// Create new workout
function handleCreateWorkout($conn, $userData) {
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
    $workoutType = sanitizeInput($input['workoutType']);
    $startTime = sanitizeInput($input['startTime']);
    $endTime = isset($input['endTime']) ? sanitizeInput($input['endTime']) : $startTime;
    $duration = intval($input['duration']);
    $calories = floatval($input['caloriesBurned']);

    // Verify user can only create workouts for themselves
    if ($userId !== $userData['user_id']) {
        http_response_code(403);
        echo json_encode([
            'status' => 'error',
            'message' => 'Forbidden'
        ]);
        return;
    }

    // Insert main workout
    $stmt = $conn->prepare("INSERT INTO workouts (user_id, workout_type, start_time, end_time, duration, calories_burned) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("isssis", $userId, $workoutType, $startTime, $endTime, $duration, $calories);

    if (!$stmt->execute()) {
        http_response_code(500);
        echo json_encode([
            'status' => 'error',
            'message' => 'Failed to create workout'
        ]);
        $stmt->close();
        return;
    }

    $workoutId = $conn->insert_id;
    $stmt->close();

    // Insert workout-specific details
    switch ($workoutType) {
        case 'running':
            $distance = floatval($input['distance']);
            $pace = floatval($input['averagePace']);
            $routeData = isset($input['routeData']) ? $input['routeData'] : null;

            $detailStmt = $conn->prepare("INSERT INTO running_workouts (workout_id, distance, average_pace, route_data) VALUES (?, ?, ?, ?)");
            $detailStmt->bind_param("idds", $workoutId, $distance, $pace, $routeData);
            $detailStmt->execute();
            $detailStmt->close();
            break;

        case 'weightlifting':
            $exerciseName = sanitizeInput($input['exerciseName']);
            $sets = intval($input['totalSets']);
            $reps = intval($input['totalReps']);
            $maxWeight = floatval($input['maxWeight']);

            $detailStmt = $conn->prepare("INSERT INTO weightlifting_workouts (workout_id, exercise_name, total_sets, total_reps, max_weight) VALUES (?, ?, ?, ?, ?)");
            $detailStmt->bind_param("isiid", $workoutId, $exerciseName, $sets, $reps, $maxWeight);
            $detailStmt->execute();
            $detailStmt->close();
            break;

        case 'cycling':
            $distance = floatval($input['distance']);
            $speed = floatval($input['averageSpeed']);

            $detailStmt = $conn->prepare("INSERT INTO cycling_workouts (workout_id, distance, average_speed) VALUES (?, ?, ?)");
            $detailStmt->bind_param("idd", $workoutId, $distance, $speed);
            $detailStmt->execute();
            $detailStmt->close();
            break;
    }

    http_response_code(201);
    echo json_encode([
        'status' => 'success',
        'message' => 'Workout created successfully',
        'data' => [
            'id' => $workoutId
        ]
    ]);
}

// Delete workout
function handleDeleteWorkout($conn, $userData) {
    if (!isset($_GET['id'])) {
        http_response_code(400);
        echo json_encode([
            'status' => 'error',
            'message' => 'Workout id is required'
        ]);
        return;
    }

    $workoutId = intval($_GET['id']);

    // Verify workout belongs to user
    $stmt = $conn->prepare("SELECT user_id FROM workouts WHERE id = ?");
    $stmt->bind_param("i", $workoutId);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows === 0) {
        http_response_code(404);
        echo json_encode([
            'status' => 'error',
            'message' => 'Workout not found'
        ]);
        $stmt->close();
        return;
    }

    $workout = $result->fetch_assoc();
    $stmt->close();

    if ($workout['user_id'] !== $userData['user_id']) {
        http_response_code(403);
        echo json_encode([
            'status' => 'error',
            'message' => 'Forbidden'
        ]);
        return;
    }

    // Delete workout (cascades to detail tables)
    $stmt = $conn->prepare("DELETE FROM workouts WHERE id = ?");
    $stmt->bind_param("i", $workoutId);

    if ($stmt->execute()) {
        http_response_code(200);
        echo json_encode([
            'status' => 'success',
            'message' => 'Workout deleted successfully'
        ]);
    } else {
        http_response_code(500);
        echo json_encode([
            'status' => 'error',
            'message' => 'Failed to delete workout'
        ]);
    }

    $stmt->close();
}

// Update workout (placeholder)
function handleUpdateWorkout($conn, $userData) {
    http_response_code(501);
    echo json_encode([
        'status' => 'error',
        'message' => 'Update not implemented yet'
    ]);
}
?>

