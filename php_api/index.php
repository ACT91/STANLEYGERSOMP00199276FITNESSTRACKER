<?php
/**
 * Fitness Tracker API - Home Page
 * Provides quick access to setup and test tools
 */

header('Content-Type: text/html; charset=utf-8');

echo "<!DOCTYPE html>
<html>
<head>
    <title>Fitness Tracker API</title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .container {
            background: white;
            max-width: 900px;
            width: 100%;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 40px;
            text-align: center;
        }
        .header h1 {
            font-size: 36px;
            margin-bottom: 10px;
        }
        .header p {
            font-size: 18px;
            opacity: 0.9;
        }
        .content {
            padding: 40px;
        }
        .status-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }
        .status-card {
            padding: 20px;
            background: #f8f9fa;
            border-radius: 10px;
            border-left: 4px solid #667eea;
        }
        .status-card h3 {
            color: #667eea;
            margin-bottom: 10px;
            font-size: 16px;
        }
        .status-card p {
            color: #333;
            font-size: 24px;
            font-weight: bold;
        }
        .status-ok {
            border-left-color: #4CAF50;
        }
        .status-ok h3 {
            color: #4CAF50;
        }
        .status-error {
            border-left-color: #f44336;
        }
        .status-error h3 {
            color: #f44336;
        }
        .actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
            margin-bottom: 30px;
        }
        .btn {
            display: block;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 10px;
            text-align: center;
            transition: transform 0.3s, box-shadow 0.3s;
            font-size: 16px;
            font-weight: 500;
        }
        .btn:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }
        .btn-success {
            background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
        }
        .btn-info {
            background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
        }
        .btn-warning {
            background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
        }
        .endpoints {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
        }
        .endpoints h3 {
            margin-bottom: 15px;
            color: #333;
        }
        .endpoint-list {
            list-style: none;
        }
        .endpoint-list li {
            padding: 10px;
            margin: 5px 0;
            background: white;
            border-radius: 5px;
            display: flex;
            align-items: center;
        }
        .method-badge {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 5px;
            font-size: 12px;
            font-weight: bold;
            color: white;
            margin-right: 10px;
            min-width: 60px;
            text-align: center;
        }
        .method-post { background: #4CAF50; }
        .method-get { background: #2196F3; }
        .method-put { background: #ff9800; }
        .method-delete { background: #f44336; }
        .footer {
            background: #f8f9fa;
            padding: 20px;
            text-align: center;
            color: #666;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class='container'>
        <div class='header'>
            <h1>🏋️ Fitness Tracker API</h1>
            <p>RESTful API Backend for Fitness Tracking Application</p>
        </div>

        <div class='content'>";

// Check database connection
$dbStatus = 'Unknown';
$tableCount = 0;
$dbConnected = false;

try {
    require_once 'config.php';
    $conn = getDBConnection();
    $dbConnected = true;
    $dbStatus = 'Connected';

    $result = $conn->query("SHOW TABLES");
    $tableCount = $result->num_rows;
    $conn->close();
} catch (Exception $e) {
    $dbStatus = 'Not Connected';
}

echo "
            <div class='status-grid'>
                <div class='status-card " . ($dbConnected ? 'status-ok' : 'status-error') . "'>
                    <h3>Database Status</h3>
                    <p>$dbStatus</p>
                </div>
                <div class='status-card " . ($tableCount === 6 ? 'status-ok' : 'status-error') . "'>
                    <h3>Tables</h3>
                    <p>$tableCount / 6</p>
                </div>
                <div class='status-card status-ok'>
                    <h3>API Version</h3>
                    <p>v1.0</p>
                </div>
            </div>

            <div class='actions'>
                <a href='setup.php' class='btn btn-success'>
                    🔧 Setup Database
                </a>
                <a href='test.php' class='btn btn-info'>
                    🧪 Test API
                </a>
                <a href='http://localhost/phpmyadmin' class='btn btn-warning' target='_blank'>
                    📊 phpMyAdmin
                </a>
            </div>";

if ($tableCount !== 6) {
    echo "
            <div style='background: #fff3cd; padding: 20px; border-radius: 10px; border-left: 4px solid #ff9800; margin-bottom: 20px;'>
                <h3 style='color: #856404; margin-bottom: 10px;'>⚠️ Database Not Configured</h3>
                <p style='color: #856404;'>Click 'Setup Database' above to automatically create all required tables.</p>
            </div>";
}

echo "
            <div class='endpoints'>
                <h3>📡 Available API Endpoints</h3>
                <ul class='endpoint-list'>
                    <li>
                        <span class='method-badge method-post'>POST</span>
                        <span>/api/register.php - User Registration</span>
                    </li>
                    <li>
                        <span class='method-badge method-post'>POST</span>
                        <span>/api/login.php - User Authentication</span>
                    </li>
                    <li>
                        <span class='method-badge method-post'>POST</span>
                        <span>/api/workouts.php - Create Workout</span>
                    </li>
                    <li>
                        <span class='method-badge method-get'>GET</span>
                        <span>/api/workouts.php?user_id={id} - Get Workouts</span>
                    </li>
                    <li>
                        <span class='method-badge method-delete'>DELETE</span>
                        <span>/api/workouts.php?id={id} - Delete Workout</span>
                    </li>
                    <li>
                        <span class='method-badge method-post'>POST</span>
                        <span>/api/goals.php - Create Goal</span>
                    </li>
                    <li>
                        <span class='method-badge method-get'>GET</span>
                        <span>/api/goals.php?user_id={id} - Get Goals</span>
                    </li>
                    <li>
                        <span class='method-badge method-put'>PUT</span>
                        <span>/api/goals.php?id={id} - Update Goal</span>
                    </li>
                </ul>
            </div>
        </div>

        <div class='footer'>
            <p>Fitness Tracker API v1.0 | Stanley Gersom P00199276</p>
            <p style='margin-top: 5px;'>Built with PHP, MySQL, and JWT Authentication</p>
        </div>
    </div>
</body>
</html>";
?>

