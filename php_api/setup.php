<?php
/**
 * Database Setup Script
 * Access this file via browser to automatically create the database and tables
 * URL: http://localhost/fitness_tracker_api/setup.php
 */

// Database configuration
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', ''); // Default XAMPP password is empty
define('DB_NAME', 'fitness_tracker');

// Set headers
header('Content-Type: text/html; charset=utf-8');

echo "<!DOCTYPE html>
<html>
<head>
    <title>Fitness Tracker - Database Setup</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
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
        .success {
            background-color: #4CAF50;
            color: white;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
        }
        .error {
            background-color: #f44336;
            color: white;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
        }
        .info {
            background-color: #2196F3;
            color: white;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
        }
        .warning {
            background-color: #ff9800;
            color: white;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
        }
        .step {
            margin: 15px 0;
            padding: 10px;
            background-color: #f9f9f9;
            border-left: 4px solid #2196F3;
        }
        pre {
            background-color: #263238;
            color: #aed581;
            padding: 15px;
            border-radius: 5px;
            overflow-x: auto;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            margin: 10px 5px;
            background-color: #2196F3;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            font-size: 16px;
        }
        .btn:hover {
            background-color: #1976D2;
        }
        .btn-danger {
            background-color: #f44336;
        }
        .btn-danger:hover {
            background-color: #d32f2f;
        }
    </style>
</head>
<body>
    <div class='container'>
        <h1>🏋️ Fitness Tracker - Database Setup</h1>";

// Check if force parameter is set
$force = isset($_GET['force']) && $_GET['force'] === 'true';

try {
    // Connect to MySQL (without selecting database)
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS);

    if ($conn->connect_error) {
        throw new Exception("Connection failed: " . $conn->connect_error);
    }

    echo "<div class='success'>✅ Successfully connected to MySQL server</div>";

    // Check if database exists
    $result = $conn->query("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" . DB_NAME . "'");
    $dbExists = $result->num_rows > 0;

    if ($dbExists && !$force) {
        echo "<div class='warning'>⚠️ Database '" . DB_NAME . "' already exists!</div>";
        echo "<div class='info'>
                <p><strong>Options:</strong></p>
                <a href='?force=true' class='btn btn-danger' onclick=\"return confirm('This will DELETE all existing data! Are you sure?')\">
                    🗑️ Delete & Recreate Database
                </a>
                <a href='setup.php' class='btn'>🔄 Refresh</a>
                <a href='test.php' class='btn'>🧪 Test API</a>
              </div>";

        // Show existing tables
        $conn->select_db(DB_NAME);
        $tables = $conn->query("SHOW TABLES");
        if ($tables->num_rows > 0) {
            echo "<div class='step'><h3>Existing Tables:</h3><ul>";
            while ($row = $tables->fetch_array()) {
                $tableName = $row[0];
                $count = $conn->query("SELECT COUNT(*) as count FROM $tableName")->fetch_assoc()['count'];
                echo "<li><strong>$tableName</strong> - $count rows</li>";
            }
            echo "</ul></div>";
        }
    } else {
        // Drop database if force flag is set
        if ($dbExists && $force) {
            echo "<div class='step'>🗑️ Dropping existing database...</div>";
            if ($conn->query("DROP DATABASE " . DB_NAME)) {
                echo "<div class='success'>✅ Database dropped successfully</div>";
            }
        }

        // Create database
        echo "<div class='step'>📦 Creating database: " . DB_NAME . "</div>";
        if ($conn->query("CREATE DATABASE " . DB_NAME)) {
            echo "<div class='success'>✅ Database created successfully</div>";
        } else {
            throw new Exception("Error creating database: " . $conn->error);
        }

        // Select database
        $conn->select_db(DB_NAME);

        // Create tables
        $tables_created = 0;

        // 1. Users table
        echo "<div class='step'>📋 Creating users table...</div>";
        $sql = "CREATE TABLE IF NOT EXISTS users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            email VARCHAR(255) UNIQUE NOT NULL,
            password_hash VARCHAR(255) NOT NULL,
            name VARCHAR(255) NOT NULL,
            age INT NOT NULL,
            weight DOUBLE NOT NULL,
            height DOUBLE NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )";
        if ($conn->query($sql)) {
            echo "<div class='success'>✅ Users table created</div>";
            $tables_created++;
        } else {
            throw new Exception("Error creating users table: " . $conn->error);
        }

        // 2. Fitness goals table (MUST be created before workouts due to foreign key)
        echo "<div class='step'>📋 Creating fitness_goals table...</div>";
        $sql = "CREATE TABLE IF NOT EXISTS fitness_goals (
            id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT NOT NULL,
            goal_type VARCHAR(100) NOT NULL,
            target_value DOUBLE NOT NULL,
            current_value DOUBLE DEFAULT 0,
            deadline DATE NOT NULL,
            achieved BOOLEAN DEFAULT FALSE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
        )";
        if ($conn->query($sql)) {
            echo "<div class='success'>✅ Fitness goals table created</div>";
            $tables_created++;
        } else {
            throw new Exception("Error creating fitness_goals table: " . $conn->error);
        }

        // 3. Workouts table (with goal_id foreign key)
        echo "<div class='step'>📋 Creating workouts table...</div>";
        $sql = "CREATE TABLE IF NOT EXISTS workouts (
            id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT NOT NULL,
            goal_id INT,
            workout_type VARCHAR(50) NOT NULL,
            start_time DATETIME NOT NULL,
            end_time DATETIME,
            duration INT NOT NULL,
            calories_burned DOUBLE NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
            FOREIGN KEY (goal_id) REFERENCES fitness_goals(id) ON DELETE SET NULL
        )";
        if ($conn->query($sql)) {
            echo "<div class='success'>✅ Workouts table created</div>";
            $tables_created++;
        } else {
            throw new Exception("Error creating workouts table: " . $conn->error);
        }

        // 4. Running workouts table
        echo "<div class='step'>📋 Creating running_workouts table...</div>";
        $sql = "CREATE TABLE IF NOT EXISTS running_workouts (
            id INT AUTO_INCREMENT PRIMARY KEY,
            workout_id INT NOT NULL,
            distance DOUBLE NOT NULL,
            average_pace DOUBLE NOT NULL,
            route_data TEXT,
            FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
        )";
        if ($conn->query($sql)) {
            echo "<div class='success'>✅ Running workouts table created</div>";
            $tables_created++;
        } else {
            throw new Exception("Error creating running_workouts table: " . $conn->error);
        }

        // 5. Weightlifting workouts table
        echo "<div class='step'>📋 Creating weightlifting_workouts table...</div>";
        $sql = "CREATE TABLE IF NOT EXISTS weightlifting_workouts (
            id INT AUTO_INCREMENT PRIMARY KEY,
            workout_id INT NOT NULL,
            exercise_name VARCHAR(255) NOT NULL,
            total_sets INT NOT NULL,
            total_reps INT NOT NULL,
            max_weight DOUBLE NOT NULL,
            FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
        )";
        if ($conn->query($sql)) {
            echo "<div class='success'>✅ Weightlifting workouts table created</div>";
            $tables_created++;
        } else {
            throw new Exception("Error creating weightlifting_workouts table: " . $conn->error);
        }

        // 6. Cycling workouts table
        echo "<div class='step'>📋 Creating cycling_workouts table...</div>";
        $sql = "CREATE TABLE IF NOT EXISTS cycling_workouts (
            id INT AUTO_INCREMENT PRIMARY KEY,
            workout_id INT NOT NULL,
            distance DOUBLE NOT NULL,
            average_speed DOUBLE NOT NULL,
            FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
        )";
        if ($conn->query($sql)) {
            echo "<div class='success'>✅ Cycling workouts table created</div>";
            $tables_created++;
        } else {
            throw new Exception("Error creating cycling_workouts table: " . $conn->error);
        }

        // 7. Achievements table
        echo "<div class='step'>📋 Creating achievements table...</div>";
        $sql = "CREATE TABLE IF NOT EXISTS achievements (
            id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT NOT NULL,
            goal_id INT NOT NULL,
            title VARCHAR(255) NOT NULL,
            description TEXT,
            achieved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
            FOREIGN KEY (goal_id) REFERENCES fitness_goals(id) ON DELETE CASCADE
        )";
        if ($conn->query($sql)) {
            echo "<div class='success'>✅ Achievements table created</div>";
            $tables_created++;
        } else {
            throw new Exception("Error creating achievements table: " . $conn->error);
        }

        // Create indexes
        echo "<div class='step'>📑 Creating indexes for performance...</div>";
        $conn->query("CREATE INDEX idx_workouts_user_id ON workouts(user_id)");
        $conn->query("CREATE INDEX idx_workouts_goal_id ON workouts(goal_id)");
        $conn->query("CREATE INDEX idx_goals_user_id ON fitness_goals(user_id)");
        $conn->query("CREATE INDEX idx_achievements_user_id ON achievements(user_id)");
        echo "<div class='success'>✅ Indexes created</div>";

        // Success summary
        echo "<div class='success'>
                <h2>🎉 Database Setup Complete!</h2>
                <p><strong>Database:</strong> " . DB_NAME . "</p>
                <p><strong>Tables Created:</strong> $tables_created</p>
              </div>";

        echo "<div class='info'>
                <h3>✅ Setup Summary:</h3>
                <ul>
                    <li>✅ Database created</li>
                    <li>✅ Users table</li>
                    <li>✅ Fitness Goals table</li>
                    <li>✅ Workouts table (with goal linking)</li>
                    <li>✅ Running workouts table</li>
                    <li>✅ Weightlifting workouts table</li>
                    <li>✅ Cycling workouts table</li>
                    <li>✅ Achievements table</li>
                    <li>✅ Foreign keys configured</li>
                    <li>✅ Performance indexes created</li>
                    <li>✅ Ready to use!</li>
                </ul>
              </div>";

        echo "<div class='step'>
                <h3>🚀 Next Steps:</h3>
                <ol>
                    <li>Update RetrofitClient.kt BASE_URL to point to your API</li>
                    <li>Run the Android app</li>
                    <li>Register a new user</li>
                    <li>Create goals and track workouts!</li>
                </ol>
                <a href='test.php' class='btn'>🧪 Test API</a>
                <a href='index.php' class='btn'>📖 API Documentation</a>
                <a href='setup.php' class='btn'>🔄 Refresh</a>
              </div>";
    }

    $conn->close();

} catch (Exception $e) {
    echo "<div class='error'>❌ Error: " . $e->getMessage() . "</div>";
    echo "<div class='info'>
            <h3>💡 Troubleshooting:</h3>
            <ul>
                <li>Make sure XAMPP MySQL is running</li>
                <li>Check if MySQL port 3306 is available</li>
                <li>Verify database credentials in config.php</li>
            </ul>
          </div>";
}

echo "
        <div class='step'>
            <h3>📝 Database Configuration:</h3>
            <pre>Host: " . DB_HOST . "
User: " . DB_USER . "
Database: " . DB_NAME . "</pre>
        </div>

        <div class='step'>
            <h3>🔗 Quick Links:</h3>
            <a href='http://localhost/phpmyadmin' class='btn' target='_blank'>📊 phpMyAdmin</a>
            <a href='api/register.php' class='btn' target='_blank'>📝 Register API</a>
            <a href='api/login.php' class='btn' target='_blank'>🔐 Login API</a>
        </div>
    </div>
</body>
</html>";
?>

