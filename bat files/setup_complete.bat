@echo off
REM ========================================
REM Fitness Tracker - Complete Setup & Sync
REM One-click setup for the entire project
REM ========================================

title Fitness Tracker - Complete Setup

echo.
echo ========================================
echo  FITNESS TRACKER - COMPLETE SETUP
echo ========================================
echo.

color 0B

REM Define paths
set "SOURCE_DIR=%~dp0php_api"
set "DEST_DIR=C:\xampp\htdocs\fitness_tracker_api"

echo Step 1/4: Checking prerequisites...
echo.

REM Check if XAMPP is installed
if not exist "C:\xampp" (
    color 0E
    echo [WARNING] XAMPP not found at C:\xampp\
    echo.
    echo Please install XAMPP first:
    echo https://www.apachefriends.org/
    echo.
    pause
    exit /b 1
)

echo [OK] XAMPP installation found
echo.

REM Check if Apache is running
tasklist /FI "IMAGENAME eq httpd.exe" 2>NUL | find /I /N "httpd.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo [OK] Apache is running
) else (
    color 0E
    echo [WARNING] Apache is not running!
    echo.
    echo Please start Apache from XAMPP Control Panel
    echo.
    set /p START_APACHE="Do you want me to try to start it? (Y/N): "
    if /i "%START_APACHE%"=="Y" (
        echo Starting Apache...
        "C:\xampp\apache_start.bat" 2>NUL
        timeout /t 3 >nul
    )
)
echo.

REM Check if MySQL is running
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo [OK] MySQL is running
) else (
    color 0E
    echo [WARNING] MySQL is not running!
    echo.
    echo Please start MySQL from XAMPP Control Panel
    echo.
    set /p START_MYSQL="Do you want me to try to start it? (Y/N): "
    if /i "%START_MYSQL%"=="Y" (
        echo Starting MySQL...
        "C:\xampp\mysql_start.bat" 2>NUL
        timeout /t 3 >nul
    )
)
echo.

echo ========================================
echo Step 2/4: Syncing API files to XAMPP...
echo ========================================
echo.

REM Create destination directory if needed
if not exist "%DEST_DIR%" (
    mkdir "%DEST_DIR%"
    echo [INFO] Created directory: %DEST_DIR%
)

REM Sync files
echo [INFO] Copying files...
robocopy "%SOURCE_DIR%" "%DEST_DIR%" /E /XO /MT:8 /NFL /NDL /NJH /NJS /nc /ns /np

if errorlevel 8 (
    color 0C
    echo [ERROR] File sync failed!
    pause
    exit /b 1
) else (
    color 0A
    echo [SUCCESS] Files synced successfully!
)
echo.

echo ========================================
echo Step 3/4: Database Setup
echo ========================================
echo.

echo The database will be created automatically when you
echo access the setup page in your browser.
echo.

set /p AUTO_SETUP="Do you want to open the setup page now? (Y/N): "
if /i "%AUTO_SETUP%"=="Y" (
    echo.
    echo [INFO] Opening setup page...
    start http://localhost/fitness_tracker_api/setup.php
    timeout /t 2 >nul
    echo.
    echo [INFO] Follow the instructions in your browser to complete database setup.
    echo.
)

echo ========================================
echo Step 4/4: Final Configuration
echo ========================================
echo.

echo [INFO] Your API is now deployed at:
echo        http://localhost/fitness_tracker_api/
echo.
echo [INFO] Important files:
echo        Setup:  http://localhost/fitness_tracker_api/setup.php
echo        Test:   http://localhost/fitness_tracker_api/test.php
echo.

echo ========================================
echo  SETUP COMPLETE!
echo ========================================
echo.

color 0A

echo Next Steps:
echo.
echo 1. Complete database setup in the browser (if not done yet)
echo.
echo 2. Update your Android app configuration:
echo    File: app\src\main\java\...\network\RetrofitClient.kt
echo
echo    For Emulator:
echo      BASE_URL = "http://10.0.2.2/fitness_tracker_api/api/"
echo
echo    For Physical Device:
echo      BASE_URL = "http://YOUR_IP/fitness_tracker_api/api/"
echo.
echo 3. Sync Gradle in Android Studio
echo.
echo 4. Run your app!
echo.

echo ========================================
echo  USEFUL COMMANDS
echo ========================================
echo.
echo - Run sync_api.bat          : Sync API files once
echo - Run sync_api_watch.bat    : Auto-sync on file changes
echo - Visit setup.php           : Create/reset database
echo - Visit test.php            : Test API endpoints
echo.

set /p OPEN_MENU="Do you want to open a quick access menu? (Y/N): "
if /i "%OPEN_MENU%"=="Y" (
    echo.
    echo Opening quick access menu...
    start http://localhost/fitness_tracker_api/
    start http://localhost/phpmyadmin/
)

echo.
echo Press any key to exit...
pause >nul

