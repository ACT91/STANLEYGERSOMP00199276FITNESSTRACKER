# Registration Field Update - Summary

## Changes Made

### Overview
Weight and height fields have been removed from the registration process. Users can now register with just their **name, email, password, and age**. They can add their weight and height later through their profile page after logging in.

---

## Files Modified

### 1. **User.kt** (Data Models)
**Location:** `app/src/main/java/com/example/stanleygersomp00199276fitnesstracker/models/User.kt`

- Made `weight` and `height` nullable (`Double?`) in the `User` data class
- Removed `weight` and `height` from `UserRegistration` data class
- Users can now register without providing these fields

### 2. **AuthViewModel.kt**
**Location:** `app/src/main/java/com/example/stanleygersomp00199276fitnesstracker/viewmodel/AuthViewModel.kt`

- Updated `register()` function to remove `weight` and `height` parameters
- Registration now only requires: email, password, name, and age

### 3. **RegisterActivity.kt**
**Location:** `app/src/main/java/com/example/stanleygersomp00199276fitnesstracker/ui/RegisterActivity.kt`

- Removed weight and height field references
- Updated `validateInput()` to only validate: name, email, password, and age
- Simplified registration logic

### 4. **activity_register.xml** (Layout)
**Location:** `app/src/main/res/layout/activity_register.xml`

- Removed `tilWeight` and `etWeight` input fields
- Removed `tilHeight` and `etHeight` input fields
- Made age field full width (no longer split with weight)
- Cleaner, simpler registration form

### 5. **register.php** (API)
**Location:** `php_api/api/register.php`

- Updated required fields list to only include: email, password, name, age
- Made weight and height optional fields (nullable)
- Changed database parameter binding from `"sssiss"` to `"sssidd"` for proper handling
- Weight and height can be NULL in database

---

## Database Changes

### Migration Script Created
**File:** `database_migration_registration.sql`

```sql
USE fitness_tracker;

ALTER TABLE users 
MODIFY COLUMN weight DOUBLE NULL,
MODIFY COLUMN height DOUBLE NULL;
```

### Migration Batch File
**File:** `apply_registration_migration.bat`

A batch file has been created to easily apply the database migration. Just run it to update your database schema.

---

## How It Works Now

### Registration Flow
1. User fills in:
   - **Name** (required)
   - **Email** (required)
   - **Password** (required, min 6 characters)
   - **Age** (required)
2. Weight and height are stored as `NULL` in the database
3. User can login successfully with these fields as NULL

### Profile Update Flow
1. After login, user can navigate to Profile page
2. Profile page allows editing of:
   - Name
   - Age
   - **Weight** (can be added/updated here)
   - **Height** (can be added/updated here)
   - Password (optional change)
3. User saves profile with weight and height
4. Database is updated with the new values

---

## Testing

### To Test Registration:
1. Clear app data or use a new email
2. Open the app and click "Register"
3. You should only see 4 fields:
   - Full Name
   - Email
   - Password
   - Age
4. Fill in the fields and register
5. You should be able to login successfully

### To Test Profile Update:
1. Login with the newly created account
2. Navigate to Profile from the menu
3. Add your weight and height
4. Click "Save Changes"
5. Your weight and height should be saved

---

## Files Copied to XAMPP
The updated PHP API files have been automatically copied to:
`C:\xampp\htdocs\fitness_tracker_api\`

---

## Benefits

✅ **Simplified Registration** - Fewer fields to fill, faster signup process  
✅ **Better UX** - Users aren't forced to provide sensitive body metrics upfront  
✅ **Flexible** - Weight and height can be added anytime in the profile  
✅ **Privacy** - Users have more control over what personal information they share  
✅ **Backward Compatible** - Existing users with weight/height data are unaffected  

---

## Important Notes

- The database migration has been applied automatically
- Existing users with weight and height data will keep their values
- New users will have NULL for weight and height until they update their profile
- The Profile page still requires weight and height for a complete profile
- All API endpoints handle nullable weight and height correctly

---

## Next Steps

If you want to make weight and height truly optional (not required even in profile):
1. Update `ProfileActivity.kt` to make validation optional for these fields
2. Update the profile UI to show "Not set" for NULL values
3. Update calculations that use weight/height to handle NULL values

---

*Generated: 2025-11-11*

