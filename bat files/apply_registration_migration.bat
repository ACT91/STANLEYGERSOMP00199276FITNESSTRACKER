@echo off
echo =========================================
echo  Database Migration - Optional Fields
echo =========================================
echo.
echo This script will update the users table to make weight and height optional.
echo.
echo Prerequisites:
echo - MySQL server should be running
echo - Database 'fitness_tracker' should exist
echo.
pause

echo.
echo Applying database migration...
echo.

C:\xampp\mysql\bin\mysql.exe -u root < database_migration_registration.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo =========================================
    echo Migration completed successfully!
    echo =========================================
    echo.
    echo Weight and height fields are now optional in the users table.
    echo Users can add these details in their profile after login.
    echo.
) else (
    echo.
    echo =========================================
    echo ERROR: Migration failed!
    echo =========================================
    echo.
    echo Please check:
    echo 1. MySQL server is running
    echo 2. You have the correct permissions
    echo 3. The database_migration_registration.sql file exists
    echo.
)

pause

