# 🚀 Automatic Database Setup Guide

## One-Click Setup (Easiest Method)

### Step 1: Start XAMPP
1. Open **XAMPP Control Panel**
2. Click **Start** on Apache
3. Click **Start** on MySQL
4. Wait until both show "Running" status

### Step 2: Run Setup
**Option A - Double-click the batch file:**
```
setup_database.bat
```
This will automatically open the setup page in your browser.

**Option B - Manual browser access:**
1. Open your web browser
2. Go to: `http://localhost/fitness_tracker_api/setup.php`

### Step 3: Database Creation
The setup page will automatically:
- ✅ Connect to MySQL
- ✅ Create `fitness_tracker` database
- ✅ Create all 8 tables:
  - `users` - User accounts
  - `fitness_goals` - User goals
  - `workouts` - Workout records (linked to goals)
  - `running_workouts` - Running-specific data
  - `weightlifting_workouts` - Weightlifting-specific data
  - `cycling_workouts` - Cycling-specific data
  - `achievements` - Completed goals
- ✅ Set up foreign keys
- ✅ Create performance indexes
- ✅ Show success confirmation

---

## What the Setup Does

### Database Structure Created:

```
fitness_tracker (database)
├── users
│   └── Stores user accounts and profiles
├── fitness_goals
│   └── Stores user fitness goals
├── workouts (linked to goals via goal_id)
│   └── Main workout records
├── running_workouts
│   └── Running-specific metrics (distance, pace)
├── weightlifting_workouts
│   └── Weightlifting-specific metrics (sets, reps, weight)
├── cycling_workouts
│   └── Cycling-specific metrics (distance, speed)
└── achievements
    └── Completed goals as achievements
```

### Foreign Key Relationships:

```
workouts.user_id → users.id
workouts.goal_id → fitness_goals.id (optional)
fitness_goals.user_id → users.id
achievements.user_id → users.id
achievements.goal_id → fitness_goals.id
running_workouts.workout_id → workouts.id
weightlifting_workouts.workout_id → workouts.id
cycling_workouts.workout_id → workouts.id
```

---

## Setup Page Features

### First Time Setup
When you access `setup.php` for the first time:
- Creates the database
- Creates all tables
- Sets up relationships
- Shows success message with summary

### Database Already Exists
If the database already exists:
- Shows warning that database exists
- Displays list of existing tables with row counts
- Provides options:
  - **Refresh** - Reload the page
  - **Delete & Recreate** - Drops and recreates everything (⚠️ DELETES ALL DATA!)
  - **Test API** - Opens the API test page

### Force Recreate
To completely rebuild the database:
1. Click "Delete & Recreate Database" button
2. Confirm the warning (this will delete all data!)
3. Database is dropped and recreated fresh

URL: `http://localhost/fitness_tracker_api/setup.php?force=true`

---

## Testing After Setup

### Method 1: Use Test Page
```
http://localhost/fitness_tracker_api/test.php
```
This page tests all API endpoints.

### Method 2: Use phpMyAdmin
1. Open: `http://localhost/phpmyadmin`
2. Click on `fitness_tracker` database
3. Browse tables to verify structure

### Method 3: Run Android App
1. Update `RetrofitClient.kt` BASE_URL:
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2/fitness_tracker_api/api/"
   ```
2. Run the app on emulator
3. Register a new user
4. Create goals and workouts

---

## Troubleshooting

### Error: "Connection failed"
**Cause:** MySQL is not running

**Solution:**
1. Open XAMPP Control Panel
2. Start MySQL service
3. Refresh setup page

### Error: "Access denied for user 'root'"
**Cause:** Default MySQL credentials don't match

**Solution:**
1. Open `php_api/setup.php`
2. Update credentials:
   ```php
   define('DB_USER', 'root');
   define('DB_PASS', ''); // Your MySQL password
   ```
3. Save and refresh

### Database Already Exists
**Cause:** Database was created before

**Options:**
1. Keep existing database (safe)
2. Use `?force=true` to recreate (⚠️ deletes data)

### Tables Missing After Setup
**Cause:** Foreign key constraint errors

**Solution:**
1. Use `?force=true` to recreate
2. Or manually run `database_schema.sql` in phpMyAdmin

---

## Manual Setup (Alternative)

If automatic setup doesn't work, you can create tables manually:

### Option 1: Using phpMyAdmin
1. Open `http://localhost/phpmyadmin`
2. Click "New" to create database
3. Name: `fitness_tracker`
4. Go to SQL tab
5. Copy contents of `database_schema.sql`
6. Paste and Execute

### Option 2: Using MySQL Command Line
```bash
mysql -u root -p
# Enter password (usually empty for XAMPP)

source C:/Users/mwact/.../database_schema.sql
```

### Option 3: Using Migration Script
```sql
# For existing databases with data
source C:/Users/mwact/.../database_migration_goals.sql
```

---

## Verification Checklist

After setup, verify:

- [ ] Database `fitness_tracker` exists
- [ ] 8 tables created (users, fitness_goals, workouts, etc.)
- [ ] Foreign keys are set up
- [ ] Indexes are created
- [ ] No error messages on setup page
- [ ] Test page works: `http://localhost/fitness_tracker_api/test.php`

---

## Quick Reference

### Setup URL
```
http://localhost/fitness_tracker_api/setup.php
```

### Force Recreate URL
```
http://localhost/fitness_tracker_api/setup.php?force=true
```

### Test API URL
```
http://localhost/fitness_tracker_api/test.php
```

### API Documentation URL
```
http://localhost/fitness_tracker_api/index.php
```

### phpMyAdmin URL
```
http://localhost/phpmyadmin
```

---

## Database Configuration

Default settings in `setup.php`:
```php
DB_HOST: localhost
DB_USER: root
DB_PASS: (empty)
DB_NAME: fitness_tracker
```

To change, edit: `php_api/setup.php` and `php_api/config.php`

---

## Next Steps After Setup

1. ✅ Database is created
2. 📱 Update Android app BASE_URL
3. 🏃 Run the app
4. 👤 Register a user
5. 🎯 Create fitness goals
6. 💪 Track workouts
7. 🏆 Earn achievements!

---

## Support

If you encounter issues:
1. Check XAMPP services are running
2. View setup page for detailed error messages
3. Check `C:/xampp/apache/logs/error.log`
4. Verify MySQL credentials
5. Try force recreate with `?force=true`

---

## Success Indicators

When setup is successful, you'll see:

✅ Successfully connected to MySQL server
✅ Database created successfully  
✅ Users table created
✅ Fitness goals table created
✅ Workouts table created
✅ Running workouts table created
✅ Weightlifting workouts table created
✅ Cycling workouts table created
✅ Achievements table created
✅ Indexes created
🎉 Database Setup Complete!

The setup is now ready for your fitness tracking app!

