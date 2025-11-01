# API Server Setup Fix - CRITICAL

## Problem
The Android app is getting HTML instead of JSON from the API because the PHP files are not being executed properly.

## Root Cause
The `php_api` folder needs to be in XAMPP's document root (`htdocs`), not in your Android Studio project.

## Solution - Follow These Steps:

### Step 1: Copy PHP API to XAMPP
1. Open File Explorer
2. Navigate to: `C:\xampp\htdocs\`
3. Create a new folder named: `fitness_tracker_api`
4. Copy ALL files from: `C:\Users\mwact\AndroidStudioProjects\STANLEYGERSOMP00199276FITNESSTRACKER\php_api\`
5. Paste them into: `C:\xampp\htdocs\fitness_tracker_api\`

Your folder structure should look like this:
```
C:\xampp\htdocs\fitness_tracker_api\
    ├── index.php
    ├── config.php
    ├── setup.php
    ├── test.php
    ├── .htaccess
    └── api\
        ├── login.php
        ├── register.php
        ├── workouts.php
        ├── goals.php
        ├── get_profile.php
        ├── update_profile.php
        └── test_endpoint.php
```

### Step 2: Test the API
1. Open your browser
2. Go to: `http://localhost/fitness_tracker_api/test.php`
3. You should see a JSON response (not HTML)

### Step 3: Test Login Endpoint
1. Open your browser
2. Go to: `http://10.223.187.222/fitness_tracker_api/api/test_endpoint.php`
3. You should see: `{"status":"success","message":"API endpoint is reachable",...}`

### Step 4: Verify Network Access
From your Android device's browser, visit:
`http://10.223.187.222/fitness_tracker_api/test.php`

You should see JSON, not HTML.

## Alternative: Check Current Setup
If the API is already in htdocs but still not working:

### Check Apache Configuration
1. Make sure mod_rewrite is enabled in Apache
2. Open: `C:\xampp\apache\conf\httpd.conf`
3. Find and uncomment (remove #): 
   ```
   LoadModule rewrite_module modules/mod_rewrite.so
   ```
4. Restart Apache from XAMPP Control Panel

### Check .htaccess is working
The .htaccess file has been created with proper rules. If it's not being read:
1. In httpd.conf, find the Directory section for htdocs
2. Change `AllowOverride None` to `AllowOverride All`
3. Restart Apache

## Quick Test Command
Run this from your PC's browser to verify the exact endpoint the Android app is calling:
```
http://10.223.187.222/fitness_tracker_api/api/login.php
```

You should get a JSON error message (since it's GET instead of POST), NOT an HTML page.

## Current Issue Summary
- Request: `POST http://10.223.187.222/fitness_tracker_api/api/login.php`
- Expected: JSON response with `Content-Type: application/json`
- Actually Getting: HTML with `Content-Type: text/html` (the homepage)

This means Apache is not finding the login.php file and is falling back to index.php.

