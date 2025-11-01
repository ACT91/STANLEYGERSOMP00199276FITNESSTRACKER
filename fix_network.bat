@echo off
REM ========================================
REM Fix Network Connection - Add Firewall Rule
REM Run as Administrator!
REM ========================================

title Fitness Tracker - Fix Network Connection

echo.
echo ========================================
echo  FIXING NETWORK CONNECTION
echo ========================================
echo.

REM Check if running as administrator
net session >nul 2>&1
if %errorLevel% neq 0 (
    color 0C
    echo [ERROR] This script must be run as Administrator!
    echo.
    echo Right-click this file and select "Run as administrator"
    echo.
    pause
    exit /b 1
)

color 0B

echo [INFO] Adding Windows Firewall rule for Apache...
echo.

REM Add firewall rule for port 80
netsh advfirewall firewall add rule name="Apache HTTP Server (Port 80)" dir=in action=allow protocol=TCP localport=80 >nul 2>&1

if %errorLevel% equ 0 (
    color 0A
    echo.
    echo ========================================
    echo  SUCCESS! Firewall Rule Added
    echo ========================================
    echo.
    echo [OK] Windows Firewall now allows connections to Apache on port 80
    echo.
    echo Your app should now be able to connect!
    echo.
) else (
    REM Rule might already exist, try to update it
    echo [INFO] Rule may already exist, updating...
    netsh advfirewall firewall set rule name="Apache HTTP Server (Port 80)" new enable=yes >nul 2>&1

    if %errorLevel% equ 0 (
        color 0A
        echo.
        echo [OK] Firewall rule updated successfully!
        echo.
    ) else (
        color 0E
        echo.
        echo [WARNING] Could not add/update firewall rule
        echo This might be because a rule already exists or permissions issue
        echo.
    )
)

echo.
echo ========================================
echo  NEXT STEPS
echo ========================================
echo.
echo 1. Make sure XAMPP Apache is running (green)
echo 2. Test in phone browser: http://192.168.114.222/fitness_tracker_api/
echo 3. If you see the API dashboard, try your app again
echo 4. If still timeout, check NETWORK_TROUBLESHOOTING.md
echo.

echo Do you want to open XAMPP Control Panel? (Y/N)
set /p OPEN_XAMPP="> "

if /i "%OPEN_XAMPP%"=="Y" (
    if exist "C:\xampp\xampp-control.exe" (
        start "" "C:\xampp\xampp-control.exe"
        echo [INFO] Opening XAMPP Control Panel...
    ) else (
        echo [WARNING] XAMPP Control Panel not found at default location
    )
)

echo.
echo Press any key to exit...
pause >nul

