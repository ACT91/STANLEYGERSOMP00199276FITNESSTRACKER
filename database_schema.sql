-- Fitness Tracker Database Schema
-- Run this SQL in phpMyAdmin or MySQL command line

CREATE DATABASE IF NOT EXISTS fitness_tracker;
USE fitness_tracker;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    weight DOUBLE NOT NULL,
    height DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Workouts table
CREATE TABLE IF NOT EXISTS workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    workout_type VARCHAR(50) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    duration INT NOT NULL,
    calories_burned DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Running workouts table
CREATE TABLE IF NOT EXISTS running_workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT NOT NULL,
    distance DOUBLE NOT NULL,
    average_pace DOUBLE NOT NULL,
    route_data TEXT,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);

-- Weightlifting workouts table
CREATE TABLE IF NOT EXISTS weightlifting_workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT NOT NULL,
    exercise_name VARCHAR(255) NOT NULL,
    total_sets INT NOT NULL,
    total_reps INT NOT NULL,
    max_weight DOUBLE NOT NULL,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);

-- Cycling workouts table (additional)
CREATE TABLE IF NOT EXISTS cycling_workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT NOT NULL,
    distance DOUBLE NOT NULL,
    average_speed DOUBLE NOT NULL,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);

-- Fitness goals table
CREATE TABLE IF NOT EXISTS fitness_goals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    goal_type VARCHAR(100) NOT NULL,
    target_value DOUBLE NOT NULL,
    current_value DOUBLE DEFAULT 0,
    deadline DATE NOT NULL,
    achieved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_workouts_user_id ON workouts(user_id);
CREATE INDEX idx_goals_user_id ON fitness_goals(user_id);

