@echo off
echo ========================================
echo API Diagnostics - Testing Setup
echo ========================================
echo.

echo Testing local API access...
echo.

REM Test 1: Check if XAMPP is running
echo [1/5] Checking if Apache is running...
curl -s http://localhost/ >nul 2>&1
if %errorlevel% == 0 (
    echo    [OK] Apache is running
) else (
    echo    [ERROR] Apache is not responding. Start it from XAMPP Control Panel.
)
echo.

REM Test 2: Check API folder exists
echo [2/5] Checking API folder location...
if exist "C:\xampp\htdocs\fitness_tracker_api\index.php" (
    echo    [OK] API folder found at C:\xampp\htdocs\fitness_tracker_api\
) else (
    echo    [ERROR] API folder not found. Run copy_api_to_xampp.bat first.
)
echo.

REM Test 3: Test main API page
echo [3/5] Testing main API page (http://localhost/fitness_tracker_api/)...
curl -s http://localhost/fitness_tracker_api/ | findstr /C:"Fitness Tracker" >nul
if %errorlevel% == 0 (
    echo    [OK] Main page accessible
) else (
    echo    [ERROR] Cannot access main page
)
echo.

REM Test 4: Test login endpoint
echo [4/5] Testing login endpoint (should return JSON)...
curl -s -H "Content-Type: application/json" http://localhost/fitness_tracker_api/api/login.php | findstr /C:"application/json" >nul
echo    Response:
curl -s -I http://localhost/fitness_tracker_api/api/login.php | findstr /C:"Content-Type"
echo.

REM Test 5: Test from network IP
echo [5/5] Testing from network IP (http://10.223.187.222/fitness_tracker_api/)...
curl -s http://10.223.187.222/fitness_tracker_api/test.php | findstr /C:"status" >nul
if %errorlevel% == 0 (
    echo    [OK] Network access working
) else (
    echo    [WARNING] Cannot access via network IP
    echo    Make sure Windows Firewall allows connections on port 80
)
echo.

echo ========================================
echo Diagnostics complete!
echo ========================================
echo.
echo If you see any errors above:
echo 1. Make sure XAMPP Apache is running
echo 2. Run copy_api_to_xampp.bat
echo 3. Check Windows Firewall settings
echo.

pause

