@echo off
echo ========================================
echo Fitness Tracker - Quick Database Setup
echo ========================================
echo.

REM Check if XAMPP is running
echo Checking XAMPP status...
tasklist /FI "IMAGENAME eq httpd.exe" 2>NUL | find /I /N "httpd.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo [OK] Apache is running
) else (
    echo [ERROR] Apache is NOT running!
    echo Please start XAMPP Control Panel and start Apache + MySQL
    pause
    exit /b 1
)

tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo [OK] MySQL is running
) else (
    echo [ERROR] MySQL is NOT running!
    echo Please start XAMPP Control Panel and start Apache + MySQL
    pause
    exit /b 1
)

echo.
echo ========================================
echo Opening Database Setup in Browser...
echo ========================================
echo.
echo This will:
echo   1. Create the 'fitness_tracker' database
echo   2. Create all required tables
echo   3. Set up foreign keys and indexes
echo   4. Display setup status
echo.

REM Open setup.php in default browser
start http://localhost/fitness_tracker_api/setup.php

echo.
echo Setup page opened in your browser!
echo.
echo If the database already exists, you can:
echo   - View existing tables and row counts
echo   - Force recreate (WARNING: deletes all data!)
echo.
echo After setup, you can test the API at:
echo   http://localhost/fitness_tracker_api/test.php
echo.
pause

