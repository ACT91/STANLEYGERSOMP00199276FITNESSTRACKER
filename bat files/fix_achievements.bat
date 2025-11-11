@echo off
echo ========================================
echo Fixing Achievements Table
echo ========================================
echo.

echo This script will:
echo 1. Create the achievements table if missing
echo 2. Verify the table was created
echo.

echo Opening phpMyAdmin SQL page...
echo.
echo Please:
echo 1. Copy the SQL from fix_achievements_table.sql
echo 2. Paste it in the SQL tab
echo 3. Click Execute
echo.

start http://localhost/phpmyadmin

echo.
echo SQL file location:
echo %~dp0fix_achievements_table.sql
echo.
echo OR run the setup page again:
start http://localhost/fitness_tracker_api/setup.php?force=true
echo.
echo (WARNING: ?force=true will recreate ALL tables and DELETE existing data!)
echo.
pause

