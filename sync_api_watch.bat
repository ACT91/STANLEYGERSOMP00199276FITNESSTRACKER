@echo off
REM ========================================
REM Fitness Tracker API Auto-Sync (Watch Mode)
REM Continuously monitors and syncs changes
REM ========================================

title Fitness Tracker - API Auto-Sync (Watch Mode)

echo.
echo ========================================
echo  FITNESS TRACKER - AUTO-SYNC ENABLED
echo ========================================
echo.

REM Set colors
color 0B

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
    pause
    exit /b 1
)

REM Check if XAMPP htdocs exists
if not exist "C:\xampp\htdocs" (
    color 0C
    echo [ERROR] XAMPP htdocs directory not found!
    pause
    exit /b 1
)

REM Create destination directory if it doesn't exist
if not exist "%DEST_DIR%" (
    echo [INFO] Creating destination directory...
    mkdir "%DEST_DIR%"
)

echo [INFO] Auto-sync is now running...
echo [INFO] Watching for changes in: %SOURCE_DIR%
echo [INFO] Press Ctrl+C to stop
echo.
echo ========================================

:WATCH_LOOP
    REM Sync files
    robocopy "%SOURCE_DIR%" "%DEST_DIR%" /E /XO /MT:8 /NFL /NDL /NJH /NJS /nc /ns /np > nul 2>&1

    REM Get current time
    echo [%date% %time:~0,8%] Files synced - Watching for changes...

    REM Wait 5 seconds before next sync
    timeout /t 5 /nobreak > nul

    REM Loop back
    goto WATCH_LOOP

