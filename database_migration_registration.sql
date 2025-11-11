-- Migration to make weight and height optional in users table
-- Run this SQL in phpMyAdmin or MySQL command line

USE fitness_tracker;

-- Modify the users table to allow NULL values for weight and height
ALTER TABLE users
MODIFY COLUMN weight DOUBLE NULL,
MODIFY COLUMN height DOUBLE NULL;

-- Update existing records with 0 values to NULL (optional cleanup)
UPDATE users SET weight = NULL WHERE weight = 0;
UPDATE users SET height = NULL WHERE height = 0;

