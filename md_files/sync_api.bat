@echo off
REM ========================================
REM Fitness Tracker API Sync Script
REM Automatically syncs php_api folder to XAMPP
REM ========================================

title Fitness Tracker - API Sync

echo.
echo ========================================
echo  FITNESS TRACKER - API SYNC TOOL
echo ========================================
echo.

REM Set colors (if supported)
color 0A

REM Define paths
set "SOURCE_DIR=%~dp0php_api"
set "DEST_DIR=C:\xampp\htdocs\fitness_tracker_api"

echo [INFO] Source Directory: %SOURCE_DIR%
echo [INFO] Destination Directory: %DEST_DIR%
echo.

REM Check if source directory exists
if not exist "%SOURCE_DIR%" (
    color 0C
    echo [ERROR] Source directory not found: %SOURCE_DIR%
    echo [ERROR] Make sure you're running this script from the project root folder!
    echo.
    pause
    exit /b 1
)

REM Check if XAMPP htdocs exists
if not exist "C:\xampp\htdocs" (
    color 0C
    echo [ERROR] XAMPP htdocs directory not found!
    echo [ERROR] Make sure XAMPP is installed at C:\xampp\
    echo.
    echo If XAMPP is installed elsewhere, edit this batch file and update the DEST_DIR path.
    echo.
    pause
    exit /b 1
)

REM Create destination directory if it doesn't exist
if not exist "%DEST_DIR%" (
    echo [INFO] Creating destination directory...
    mkdir "%DEST_DIR%"
    if errorlevel 1 (
        color 0C
        echo [ERROR] Failed to create destination directory!
        pause
        exit /b 1
    )
    echo [SUCCESS] Destination directory created.
    echo.
)

REM Perform the sync
echo [INFO] Syncing files...
echo.

REM Use robocopy for efficient syncing (included in Windows)
robocopy "%SOURCE_DIR%" "%DEST_DIR%" /E /XO /MT:8 /NFL /NDL /NJH /NJS /nc /ns /np

if errorlevel 8 (
    color 0C
    echo.
    echo [ERROR] Sync failed! Some files could not be copied.
    echo.
    pause
    exit /b 1
) else (
    color 0A
    echo.
    echo ========================================
    echo  SYNC COMPLETED SUCCESSFULLY!
    echo ========================================
    echo.
    echo [SUCCESS] API files synced to XAMPP
    echo [SUCCESS] Location: %DEST_DIR%
    echo.
    echo You can now:
    echo  1. Open browser: http://localhost/fitness_tracker_api/setup.php
    echo  2. Create database automatically
    echo  3. Test API: http://localhost/fitness_tracker_api/test.php
    echo.
)

REM Ask if user wants to open setup page
echo.
set /p OPEN_SETUP="Do you want to open the setup page in browser? (Y/N): "
if /i "%OPEN_SETUP%"=="Y" (
    start http://localhost/fitness_tracker_api/setup.php
    echo [INFO] Opening setup page in browser...
)

echo.
echo Press any key to exit...
pause >nul

