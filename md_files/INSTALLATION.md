# 🚀 FINAL INSTALLATION INSTRUCTIONS

## IMPORTANT: Read This First!

The app is **100% complete** but you need to follow these steps **IN ORDER** to get it running.

---

## ⚡ QUICK INSTALL (Follow These 6 Steps)

### STEP 1: Install XAMPP
1. Download XAMPP from: https://www.apachefriends.org/
2. Install it (use default settings)
3. Open XAMPP Control Panel
4. Click **Start** next to Apache
5. Click **Start** next to MySQL
6. Both should show **green** status

✅ **Check**: Open browser → http://localhost → Should see XAMPP dashboard

---

### STEP 2: Create Database
1. Open browser → http://localhost/phpmyadmin
2. Click **"New"** in left sidebar
3. Database name: `fitness_tracker`
4. Click **"Create"**
5. Click **"SQL"** tab at top
6. Open file: `database_schema.sql` (in project folder)
7. **Copy ALL content** from that file
8. **Paste** into SQL box
9. Click **"Go"** button
10. You should see: "6 rows affected" or similar success message

✅ **Check**: Click `fitness_tracker` in left sidebar → Should see 6 tables:
- users
- workouts
- running_workouts
- weightlifting_workouts
- cycling_workouts
- fitness_goals

---

### STEP 3: Deploy PHP API
1. Open Windows Explorer
2. Navigate to: `C:\xampp\htdocs\`
3. **Copy** the `php_api` folder from your project
4. **Paste** it into `C:\xampp\htdocs\`
5. **Rename** from `php_api` to `fitness_tracker_api`

✅ **Check**: File `C:\xampp\htdocs\fitness_tracker_api\config.php` should exist

✅ **Check**: Open browser → http://localhost/fitness_tracker_api/api/register.php
   - Should see: `{"status":"error","message":"Method not allowed"}`
   - This is CORRECT! It means the API is working!

---

### STEP 4: Configure Android App

**For Android Emulator:**
1. Open file: `app\src\main\java\...\network\RetrofitClient.kt`
2. Find line: `private const val BASE_URL = ...`
3. Make sure it says: `"http://10.0.2.2/fitness_tracker_api/api/"`
4. Save file

**For Physical Device:**
1. Find your computer's IP address:
   - Press Windows Key + R
   - Type: `cmd`
   - Press Enter
   - Type: `ipconfig`
   - Look for "IPv4 Address" (example: 192.168.1.100)
   - **Write it down!**

2. Update RetrofitClient.kt:
   - Change BASE_URL to: `"http://YOUR_IP/fitness_tracker_api/api/"`
   - Replace YOUR_IP with the actual IP (example: `"http://192.168.1.100/fitness_tracker_api/api/"`)
   - Save file

3. Connect your phone and PC to **same WiFi network**

---

### STEP 5: Sync Gradle in Android Studio

1. Open Android Studio
2. Wait for it to load completely
3. You'll see a banner: **"Gradle files have changed since last project sync"**
4. Click **"Sync Now"**
5. **Wait patiently** (this might take 2-5 minutes the first time)
6. Watch the bottom status bar - it will show progress
7. When done, you should see: **"BUILD SUCCESSFUL"** or **"Sync: OK"**

⚠️ **If you see errors:**
- Click **File** → **Invalidate Caches / Restart**
- Select **"Invalidate and Restart"**
- Wait for Android Studio to restart
- Try sync again

---

### STEP 6: Run the App!

1. **Connect your device OR start an emulator**
   - For device: Enable USB Debugging in Developer Options
   - For emulator: Click the AVD Manager icon and start a device

2. **Select your device** from the dropdown at the top

3. **Click the green Run button** (or press Shift+F10)

4. **Wait for the app to install** (first time takes ~1 minute)

5. **App should launch** showing the Login screen!

---

## 🎯 TESTING YOUR INSTALLATION

### Test 1: Register a User
1. On Login screen, click **"Don't have an account? Register"**
2. Fill in:
   - Name: `Test User`
   - Email: `test@example.com`
   - Password: `password123`
   - Age: `25`
   - Weight: `70`
   - Height: `175`
3. Click **"Register"**
4. Should see: **"Registration successful! Please login."**

✅ **Verify in Database:**
- Open phpMyAdmin → fitness_tracker → users table
- You should see 1 row with your test user

### Test 2: Login
1. Enter email: `test@example.com`
2. Enter password: `password123`
3. Click **"Login"**
4. Should see the **Dashboard** with your name displayed!

### Test 3: Add a Workout
1. On Dashboard, click **"Add New Workout"**
2. Select: **Running**
3. Fill in:
   - Duration: `30`
   - Calories: `250`
   - Distance: `5`
   - Average Pace: `6`
4. Click **"Save Workout"**
5. Should see: **"Workout saved successfully!"**

✅ **Verify in Database:**
- phpMyAdmin → workouts table → 1 row
- phpMyAdmin → running_workouts table → 1 row

### Test 4: View Workouts
1. Go back to Dashboard
2. Click **"View Workout History"**
3. Should see your running workout listed!
4. Click **"Delete"** button
5. Confirm deletion
6. Workout should disappear

✅ **Verify in Database:**
- Both workouts and running_workouts tables should be empty

---

## ❌ TROUBLESHOOTING

### Problem: "Network error" in app
**Solutions:**
1. Check XAMPP Apache is **running** (green light)
2. Test API in browser: http://localhost/fitness_tracker_api/api/register.php
3. If using physical device:
   - Make sure phone and PC are on **same WiFi**
   - Try turning off Windows Firewall temporarily
   - Verify IP address is correct in RetrofitClient.kt

### Problem: "Database connection failed"
**Solutions:**
1. Check XAMPP MySQL is **running** (green light)
2. Open phpMyAdmin and verify database exists
3. Check config.php has correct credentials (should be default XAMPP settings)

### Problem: Gradle sync fails
**Solutions:**
1. Check internet connection (Gradle downloads dependencies)
2. Click **File** → **Sync Project with Gradle Files**
3. If still fails: **File** → **Invalidate Caches / Restart**
4. Last resort: Delete `.gradle` folder in project and sync again

### Problem: App crashes immediately
**Solutions:**
1. Check Logcat in Android Studio for error messages
2. Click **Build** → **Rebuild Project**
3. Click **Run** → **Clean and Rebuild Project**
4. Try running on a different emulator/device

### Problem: "Unresolved reference" errors in code
**Solution:**
- These will disappear after Gradle sync completes
- Wait for sync to finish completely
- If they persist: **File** → **Invalidate Caches / Restart**

---

## 📱 SYSTEM REQUIREMENTS

### For Development:
- **Android Studio**: Arctic Fox or newer
- **JDK**: 11 or newer
- **Minimum SDK**: Android 12 (API 31)
- **Target SDK**: Android 14 (API 35)
- **RAM**: 8GB minimum (16GB recommended)

### For XAMPP:
- **PHP**: 7.4 or newer (included in XAMPP)
- **MySQL**: 5.7 or newer (included in XAMPP)
- **Disk Space**: 1GB for XAMPP

---

## 🎓 WHAT YOU'VE BUILT

After completing these steps, you'll have:

✅ A **fully functional fitness tracker app**
✅ User registration & login system
✅ JWT authentication
✅ Three types of workouts (Running, Weightlifting, Cycling)
✅ Workout history with delete functionality
✅ Goals management system
✅ RESTful PHP API backend
✅ MySQL database with proper relationships
✅ Secure password hashing
✅ Professional Android app architecture (MVVM)

---

## 📚 NEXT STEPS AFTER INSTALLATION

1. **Read README.md** for complete documentation
2. **Read QUICK_START.md** for usage guide
3. **Read API_TESTING_GUIDE.md** to test API with Postman
4. **Read PROJECT_SUMMARY.md** for technical overview
5. **Explore the code** to understand the architecture
6. **Add more features** based on the enhancement suggestions

---

## 🆘 STILL HAVING ISSUES?

1. **Check all steps again** - 90% of issues are from skipping a step
2. **Verify XAMPP is running** - Both Apache and MySQL must be green
3. **Check the database** - Must have all 6 tables
4. **Check the API** - Test in browser first before running app
5. **Check Logcat** - Android Studio's Logcat shows detailed errors

---

## ✅ INSTALLATION SUCCESS CHECKLIST

Before running the app, verify:
- [ ] XAMPP Apache is **RUNNING** (green)
- [ ] XAMPP MySQL is **RUNNING** (green)
- [ ] Database `fitness_tracker` **EXISTS**
- [ ] Database has **6 TABLES**
- [ ] PHP files are in `C:\xampp\htdocs\fitness_tracker_api\`
- [ ] API test URL returns JSON error message
- [ ] RetrofitClient.kt has **correct BASE_URL**
- [ ] Gradle sync **COMPLETED SUCCESSFULLY**
- [ ] Android Studio shows **no red errors** after sync
- [ ] Device/Emulator is **CONNECTED**

If ALL items are checked ✅, the app **WILL WORK**!

---

## 🎉 SUCCESS!

When you see the Login screen launch on your device, congratulations! 🎊

You've successfully installed a complete full-stack Android application with:
- Kotlin frontend
- PHP backend
- MySQL database
- RESTful API
- JWT authentication

**Now go ahead and create your fitness tracker account!** 💪

---

**Happy Coding!** 🚀

*Project by: Stanley Gersom P00199276*
*Date: October 31, 2024*

