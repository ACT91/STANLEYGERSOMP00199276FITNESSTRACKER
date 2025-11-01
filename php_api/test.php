<?php
/**
 * API Test Script
 * Tests all API endpoints to verify they're working correctly
 * URL: http://localhost/fitness_tracker_api/test.php
 */

header('Content-Type: text/html; charset=utf-8');

echo "<!DOCTYPE html>
<html>
<head>
    <title>Fitness Tracker - API Test</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1000px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #2196F3;
            border-bottom: 3px solid #2196F3;
            padding-bottom: 10px;
        }
        .endpoint {
            margin: 20px 0;
            padding: 15px;
            background-color: #f9f9f9;
            border-left: 4px solid #2196F3;
            border-radius: 5px;
        }
        .endpoint h3 {
            margin-top: 0;
            color: #1976D2;
        }
        .method {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 3px;
            font-weight: bold;
            color: white;
            font-size: 12px;
            margin-right: 10px;
        }
        .post { background-color: #4CAF50; }
        .get { background-color: #2196F3; }
        .put { background-color: #ff9800; }
        .delete { background-color: #f44336; }
        .status {
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
        }
        .success { background-color: #4CAF50; color: white; }
        .error { background-color: #f44336; color: white; }
        .info { background-color: #2196F3; color: white; }
        pre {
            background-color: #263238;
            color: #aed581;
            padding: 15px;
            border-radius: 5px;
            overflow-x: auto;
            font-size: 12px;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            margin: 10px 5px;
            background-color: #2196F3;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .btn:hover {
            background-color: #1976D2;
        }
    </style>
</head>
<body>
    <div class='container'>
        <h1>🧪 Fitness Tracker - API Test</h1>";

// Check database connection
try {
    require_once 'config.php';
    $conn = getDBConnection();
    echo "<div class='status success'>✅ Database connection successful</div>";

    // Check if tables exist
    $result = $conn->query("SHOW TABLES");
    $tables = [];
    while ($row = $result->fetch_array()) {
        $tables[] = $row[0];
    }

    if (count($tables) === 6) {
        echo "<div class='status success'>✅ All 6 tables exist</div>";
    } else {
        echo "<div class='status error'>❌ Expected 6 tables, found " . count($tables) . "</div>";
        echo "<div class='info'>Run <a href='setup.php' style='color:white;text-decoration:underline;'>setup.php</a> to create tables</div>";
    }

    $conn->close();
} catch (Exception $e) {
    echo "<div class='status error'>❌ Database error: " . $e->getMessage() . "</div>";
}

echo "
        <h2>📡 Available API Endpoints</h2>

        <div class='endpoint'>
            <h3><span class='method post'>POST</span> /api/register.php - User Registration</h3>
            <p><strong>Test with cURL:</strong></p>
            <pre>curl -X POST http://localhost/fitness_tracker_api/api/register.php \\
  -H \"Content-Type: application/json\" \\
  -d '{\"email\":\"test@example.com\",\"password\":\"password123\",\"name\":\"Test User\",\"age\":25,\"weight\":70,\"height\":175}'</pre>
        </div>

        <div class='endpoint'>
            <h3><span class='method post'>POST</span> /api/login.php - User Authentication</h3>
            <p><strong>Test with cURL:</strong></p>
            <pre>curl -X POST http://localhost/fitness_tracker_api/api/login.php \\
  -H \"Content-Type: application/json\" \\
  -d '{\"email\":\"test@example.com\",\"password\":\"password123\"}'</pre>
        </div>

        <div class='endpoint'>
            <h3><span class='method post'>POST</span> /api/workouts.php - Create Workout</h3>
            <p><strong>Requires JWT token in Authorization header</strong></p>
            <pre>curl -X POST http://localhost/fitness_tracker_api/api/workouts.php \\
  -H \"Content-Type: application/json\" \\
  -H \"Authorization: Bearer YOUR_TOKEN\" \\
  -d '{\"userId\":1,\"workoutType\":\"running\",\"startTime\":\"2024-10-31 08:00:00\",\"endTime\":\"2024-10-31 08:30:00\",\"duration\":30,\"caloriesBurned\":250,\"distance\":5,\"averagePace\":6}'</pre>
        </div>

        <div class='endpoint'>
            <h3><span class='method get'>GET</span> /api/workouts.php?user_id=1 - Get User Workouts</h3>
            <p><strong>Requires JWT token in Authorization header</strong></p>
            <pre>curl -X GET \"http://localhost/fitness_tracker_api/api/workouts.php?user_id=1\" \\
  -H \"Authorization: Bearer YOUR_TOKEN\"</pre>
        </div>

        <div class='endpoint'>
            <h3><span class='method delete'>DELETE</span> /api/workouts.php?id=1 - Delete Workout</h3>
            <p><strong>Requires JWT token in Authorization header</strong></p>
            <pre>curl -X DELETE \"http://localhost/fitness_tracker_api/api/workouts.php?id=1\" \\
  -H \"Authorization: Bearer YOUR_TOKEN\"</pre>
        </div>

        <div class='endpoint'>
            <h3><span class='method get'>GET</span> /api/goals.php?user_id=1 - Get User Goals</h3>
            <p><strong>Requires JWT token in Authorization header</strong></p>
            <pre>curl -X GET \"http://localhost/fitness_tracker_api/api/goals.php?user_id=1\" \\
  -H \"Authorization: Bearer YOUR_TOKEN\"</pre>
        </div>

        <div class='endpoint'>
            <h3><span class='method post'>POST</span> /api/goals.php - Create Goal</h3>
            <p><strong>Requires JWT token in Authorization header</strong></p>
            <pre>curl -X POST http://localhost/fitness_tracker_api/api/goals.php \\
  -H \"Content-Type: application/json\" \\
  -H \"Authorization: Bearer YOUR_TOKEN\" \\
  -d '{\"userId\":1,\"goalType\":\"weight_loss\",\"targetValue\":70,\"currentValue\":75,\"deadline\":\"2024-12-31\"}'</pre>
        </div>

        <h2>🔗 Quick Links</h2>
        <a href='setup.php' class='btn'>🔧 Setup Database</a>
        <a href='http://localhost/phpmyadmin' class='btn' target='_blank'>📊 phpMyAdmin</a>
        <a href='api/register.php' class='btn' target='_blank'>Test Register</a>
        <a href='api/login.php' class='btn' target='_blank'>Test Login</a>

        <h2>📝 Testing Notes</h2>
        <div class='info'>
            <ul style='margin:10px 0;'>
                <li>Use Postman or cURL to test API endpoints</li>
                <li>Register a user first to get authentication token</li>
                <li>Use the token in Authorization header for protected endpoints</li>
                <li>Check <a href='http://localhost/phpmyadmin' style='color:white;text-decoration:underline;' target='_blank'>phpMyAdmin</a> to verify data is saved</li>
            </ul>
        </div>
    </div>
</body>
</html>";
?>

