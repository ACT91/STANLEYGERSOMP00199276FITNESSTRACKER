# QUICK START GUIDE - Fitness Tracker App

## STEP-BY-STEP SETUP (5 Minutes)

### STEP 1: Setup XAMPP & Database (2 minutes)

1. **Start XAMPP**
   - Open XAMPP Control Panel
   - Click "Start" for Apache
   - Click "Start" for MySQL
   - Both should show green "Running" status

2. **Create Database**
   - Open browser: http://localhost/phpmyadmin
   - Click "New" in left sidebar
   - Database name: `fitness_tracker`
   - Click "Create"
   - Click "SQL" tab
   - Copy entire content from `database_schema.sql` file
   - Paste in SQL box
   - Click "Go"
   - You should see 6 tables created

3. **Deploy PHP API**
   - Copy the `php_api` folder
   - Paste into: `C:\xampp\htdocs\`
   - Rename to: `fitness_tracker_api`
   - Final path: `C:\xampp\htdocs\fitness_tracker_api\`

4. **Test API**
   - Open browser: http://localhost/fitness_tracker_api/api/register.php
   - You should see: `{"status":"error","message":"Method not allowed"}`
   - ✅ This means API is working!

### STEP 2: Configure Android App (1 minute)

1. **Find Your IP Address** (only if using physical device)
   - Open Command Prompt (Windows Key + R, type "cmd")
   - Type: `ipconfig`
   - Look for "IPv4 Address" (e.g., 192.168.1.100)
   - Write it down

2. **Update API URL**
   - Open Android Studio
   - Open file: `RetrofitClient.kt`
   - Find line: `private const val BASE_URL = ...`
   - **For Emulator**: Use `"http://10.0.2.2/fitness_tracker_api/api/"`
   - **For Physical Device**: Use `"http://YOUR_IP/fitness_tracker_api/api/"`
     - Replace YOUR_IP with the IP from step 1
     - Example: `"http://192.168.1.100/fitness_tracker_api/api/"`

### STEP 3: Build & Run (2 minutes)

1. **Sync Gradle**
   - Android Studio will show "Gradle files have changed"
   - Click "Sync Now"
   - Wait for sync to complete (~1-2 minutes)

2. **Run App**
   - Connect Android device OR start emulator
   - Click green "Run" button (or press Shift+F10)
   - Wait for app to install and launch

### STEP 4: Test the App!

1. **Register New User**
   - Enter email: `test@example.com`
   - Enter password: `password123`
   - Enter name: `Test User`
   - Enter age: `25`
   - Enter weight: `70`
   - Enter height: `175`
   - Click "Register"
   - You should see: "Registration successful! Please login."

2. **Login**
   - Enter email: `test@example.com`
   - Enter password: `password123`
   - Click "Login"
   - You should see the Dashboard

3. **Add a Workout**
   - Click "Add New Workout"
   - Select workout type: "Running"
   - Enter duration: `30`
   - Enter calories: `250`
   - Enter distance: `5`
   - Enter pace: `6`
   - Click "Save Workout"
   - You should see: "Workout saved successfully!"

4. **View Workouts**
   - Go back to Dashboard
   - Click "View Workout History"
   - You should see your workout listed!

---

## TROUBLESHOOTING

### ❌ "Network error" in app
**Solution:**
- Check XAMPP Apache is running (green)
- Verify BASE_URL in RetrofitClient.kt
- For physical device: Make sure phone and PC are on same WiFi
- For physical device: Windows Firewall might be blocking - temporarily disable

### ❌ "Database connection failed"
**Solution:**
- Check XAMPP MySQL is running (green)
- Verify database name is `fitness_tracker`
- Check tables were created (6 tables should exist)

### ❌ App crashes on startup
**Solution:**
- In Android Studio, click "Build" → "Rebuild Project"
- Wait for build to complete
- Run app again

### ❌ "Cannot resolve symbol" errors
**Solution:**
- Click "File" → "Invalidate Caches / Restart"
- Select "Invalidate and Restart"
- Wait for indexing to complete

### ❌ Gradle sync failed
**Solution:**
- Check internet connection
- Click "File" → "Sync Project with Gradle Files"
- If still fails, restart Android Studio

---

## TESTING CHECKLIST

- ✅ XAMPP Apache running
- ✅ XAMPP MySQL running
- ✅ Database created with 6 tables
- ✅ PHP files in C:\xampp\htdocs\fitness_tracker_api\
- ✅ API test shows "Method not allowed" message
- ✅ BASE_URL configured correctly
- ✅ Gradle sync completed
- ✅ App installs and runs
- ✅ User registration works
- ✅ Login works
- ✅ Dashboard shows user info
- ✅ Can add workouts
- ✅ Can view workout history

---

## DEMO CREDENTIALS

After first run, you can use:
- Email: `test@example.com`
- Password: `password123`

---

## WHAT TO DO NEXT

1. **Explore All Features**
   - Try all three workout types (Running, Weightlifting, Cycling)
   - Delete workouts
   - View goals section

2. **Test with Multiple Users**
   - Register another user
   - Verify data separation (users can't see each other's workouts)

3. **Test API with Postman** (Optional)
   - Import the API endpoints from README.md
   - Test each endpoint individually
   - Verify responses

---

## PROJECT STRUCTURE

```
Your Project Folder/
├── app/                          ← Android app code
│   └── src/main/java/.../
│       ├── models/               ← Data models (User, Workout, Goal)
│       ├── network/              ← Retrofit API client
│       ├── repository/           ← Data repository
│       ├── ui/                   ← Activities (screens)
│       ├── viewmodel/            ← ViewModels
│       └── utils/                ← Session manager
│
├── php_api/                      ← Copy this to C:\xampp\htdocs\fitness_tracker_api\
│   ├── config.php                ← Database config
│   └── api/
│       ├── register.php          ← User registration
│       ├── login.php             ← User login
│       ├── workouts.php          ← Workout CRUD
│       └── goals.php             ← Goals CRUD
│
├── database_schema.sql           ← Run this in phpMyAdmin
└── README.md                     ← Full documentation
```

---

## NEED HELP?

1. Check the full README.md for detailed documentation
2. Check Android Studio Logcat for errors
3. Check XAMPP error logs:
   - Apache: C:\xampp\apache\logs\error.log
   - PHP: C:\xampp\php\logs\php_error_log

---

## SUCCESS! 🎉

If you completed all steps and tests, congratulations!
You now have a fully functional fitness tracker app with:
- ✅ Secure user authentication
- ✅ Multiple workout types
- ✅ Workout history
- ✅ Goals tracking
- ✅ RESTful API backend
- ✅ MySQL database

**Enjoy tracking your fitness!** 💪

