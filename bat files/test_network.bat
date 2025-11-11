@echo off
REM ========================================
REM Test Network Connection to API
REM ========================================

title Fitness Tracker - Test Network Connection

echo.
echo ========================================
echo  TESTING NETWORK CONNECTION
echo ========================================
echo.

set "API_IP=192.168.114.222"

echo [INFO] Your server IP: %API_IP%
echo [INFO] Testing connection...
echo.

REM Test 1: Ping the IP
echo ----------------------------------------
echo TEST 1: Ping Server
echo ----------------------------------------
ping -n 2 %API_IP% >nul 2>&1
if %errorLevel% equ 0 (
    color 0A
    echo [PASS] Server is reachable via ping
) else (
    color 0C
    echo [FAIL] Cannot ping server
    echo       - Check if both devices are on same network
)
echo.

REM Test 2: Check if Apache is listening on port 80
echo ----------------------------------------
echo TEST 2: Check Apache Port
echo ----------------------------------------
netstat -an | findstr ":80.*LISTENING" >nul 2>&1
if %errorLevel% equ 0 (
    color 0A
    echo [PASS] Apache is listening on port 80
) else (
    color 0E
    echo [FAIL] Apache is not listening on port 80
    echo       - Start Apache in XAMPP Control Panel
)
echo.

REM Test 3: Check firewall rule
echo ----------------------------------------
echo TEST 3: Check Firewall Rule
echo ----------------------------------------
netsh advfirewall firewall show rule name="Apache HTTP Server (Port 80)" >nul 2>&1
if %errorLevel% equ 0 (
    color 0A
    echo [PASS] Firewall rule exists
) else (
    color 0E
    echo [FAIL] Firewall rule not found
    echo       - Run fix_network.bat as Administrator
)
echo.

REM Test 4: Try to access the API
echo ----------------------------------------
echo TEST 4: Access API Endpoint
echo ----------------------------------------
echo [INFO] Attempting to access http://%API_IP%/fitness_tracker_api/
echo.

REM Try using curl if available
where curl >nul 2>&1
if %errorLevel% equ 0 (
    echo Using curl to test...
    curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://%API_IP%/fitness_tracker_api/ 2>nul
    if %errorLevel% equ 0 (
        color 0A
        echo [PASS] API is accessible!
    ) else (
        color 0E
        echo [FAIL] Cannot access API
    )
) else (
    echo [INFO] curl not available, opening in browser...
    start http://%API_IP%/fitness_tracker_api/
    echo.
    echo [?] Did the API dashboard open in browser? (Y/N)
    set /p API_WORKS="> "
    if /i "%API_WORKS%"=="Y" (
        color 0A
        echo [PASS] API is accessible!
    ) else (
        color 0C
        echo [FAIL] API not accessible
    )
)
echo.

echo ========================================
echo  TEST SUMMARY
echo ========================================
echo.
echo If all tests PASS:
echo   - Your server is ready!
echo   - Test app on phone browser: http://%API_IP%/fitness_tracker_api/
echo   - If phone browser works, app should work too
echo.
echo If any test FAIL:
echo   - Read NETWORK_TROUBLESHOOTING.md
echo   - Run fix_network.bat as Administrator
echo   - Make sure Apache is running in XAMPP
echo.

echo ----------------------------------------
echo.
echo Do you want to:
echo   1. Run fix_network.bat (Fix firewall)
echo   2. Open XAMPP Control Panel
echo   3. Open troubleshooting guide
echo   4. Exit
echo.
set /p CHOICE="Enter choice (1-4): "

if "%CHOICE%"=="1" (
    echo.
    echo [INFO] Please run fix_network.bat as Administrator
    echo       Right-click → Run as administrator
    pause
    exit
)

if "%CHOICE%"=="2" (
    if exist "C:\xampp\xampp-control.exe" (
        start "" "C:\xampp\xampp-control.exe"
    ) else (
        echo [ERROR] XAMPP not found at default location
    )
)

if "%CHOICE%"=="3" (
    if exist "NETWORK_TROUBLESHOOTING.md" (
        start NETWORK_TROUBLESHOOTING.md
    ) else (
        echo [ERROR] Troubleshooting guide not found
    )
)

echo.
pause

