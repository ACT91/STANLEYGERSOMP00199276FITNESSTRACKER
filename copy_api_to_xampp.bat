@echo off
echo ========================================
echo Fitness Tracker API - Copy to XAMPP
echo ========================================
echo.

REM Check if XAMPP exists
if not exist "C:\xampp\htdocs\" (
    echo ERROR: XAMPP htdocs folder not found at C:\xampp\htdocs\
    echo Please install XAMPP first or update the path in this script.
    pause
    exit /b 1
)

REM Create the API directory
echo Creating API directory...
if not exist "C:\xampp\htdocs\fitness_tracker_api\" (
    mkdir "C:\xampp\htdocs\fitness_tracker_api"
)

if not exist "C:\xampp\htdocs\fitness_tracker_api\api\" (
    mkdir "C:\xampp\htdocs\fitness_tracker_api\api"
)

echo.
echo Copying API files...

REM Copy main files
copy /Y "%~dp0php_api\*.php" "C:\xampp\htdocs\fitness_tracker_api\"
copy /Y "%~dp0php_api\.htaccess" "C:\xampp\htdocs\fitness_tracker_api\"

REM Copy API endpoint files
copy /Y "%~dp0php_api\api\*.php" "C:\xampp\htdocs\fitness_tracker_api\api\"

echo.
echo ========================================
echo Files copied successfully!
echo ========================================
echo.
echo Next steps:
echo 1. Make sure Apache is running in XAMPP Control Panel
echo 2. Open your browser and visit:
echo    http://localhost/fitness_tracker_api/test.php
echo.
echo 3. You should see a JSON response (not HTML)
echo.
echo 4. Then test from your phone's browser:
echo    http://10.223.187.222/fitness_tracker_api/test.php
echo.
echo ========================================

pause

