-- Manual fix: Add achievements table if missing
-- Run this in phpMyAdmin if setup.php didn't create it

USE fitness_tracker;

-- Create achievements table
CREATE TABLE IF NOT EXISTS achievements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    goal_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    achieved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (goal_id) REFERENCES fitness_goals(id) ON DELETE CASCADE
);

-- Create index
CREATE INDEX IF NOT EXISTS idx_achievements_user_id ON achievements(user_id);

-- Verify
SELECT 'Achievements table created successfully!' AS Status;
SHOW TABLES;

