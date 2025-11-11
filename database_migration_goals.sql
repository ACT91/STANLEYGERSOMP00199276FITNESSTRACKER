-- Migration Script: Add Goal-Linked Workout System
-- Run this if you already have an existing database with data
-- For new installations, use database_schema.sql instead

USE fitness_tracker;

-- Step 1: Add goal_id column to workouts table if not exists
ALTER TABLE workouts
ADD COLUMN IF NOT EXISTS goal_id INT AFTER user_id;

-- Step 2: Add foreign key constraint
ALTER TABLE workouts
ADD CONSTRAINT fk_workout_goal
FOREIGN KEY (goal_id) REFERENCES fitness_goals(id) ON DELETE SET NULL;

-- Step 3: Create achievements table
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

-- Step 4: Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_workouts_goal_id ON workouts(goal_id);
CREATE INDEX IF NOT EXISTS idx_achievements_user_id ON achievements(user_id);

-- Verify the changes
SHOW COLUMNS FROM workouts;
SHOW COLUMNS FROM achievements;

-- Sample: Create test goals for existing user (optional)
-- Replace user_id = 1 with your actual user ID
INSERT INTO fitness_goals (user_id, goal_type, target_value, current_value, deadline) VALUES
(1, 'workout_count', 20, 0, '2025-12-31'),
(1, 'distance', 50.0, 0, '2025-12-31'),
(1, 'calories', 5000, 0, '2025-12-31'),
(1, 'duration', 300, 0, '2025-12-31');

SELECT 'Database migration completed successfully!' AS Status;

