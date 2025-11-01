# Fitness Tracker - Complete Project Documentation

## Project Overview
A comprehensive fitness tracking application built with:
- **Frontend**: Kotlin Android App
- **Backend**: PHP RESTful API
- **Database**: MySQL (via XAMPP)

## Architecture

### Kotlin Android App (Frontend)
- **MVVM Architecture** with ViewModels and LiveData
- **Retrofit** for API communication
- **Coroutines** for asynchronous operations
- **ViewBinding** for UI interaction
- **Repository Pattern** for data management
- **SharedPreferences** for session management

### PHP API (Backend)
- RESTful API endpoints
- JWT authentication
- PDO/MySQLi for database operations
- CORS enabled for mobile access
- Input validation and sanitization
- Password hashing with bcrypt

### MySQL Database
- Normalized database schema
- Foreign key constraints
- Cascade deletion
- Indexed for performance

---

## SETUP INSTRUCTIONS

### 🚀 NEW: AUTOMATED SETUP (RECOMMENDED!) ⭐

**The easiest way to set up your project:**

1. **One-Click Setup:**
   ```
   Double-click: setup_complete.bat
   ```
   This automatically:
   - Checks XAMPP installation
   - Syncs API files to XAMPP
   - Opens database setup page
   - Guides you through remaining steps

2. **Auto-Create Database:**
   - Visit: http://localhost/fitness_tracker_api/setup.php
   - Click "Create Database" button
   - Database and all tables created automatically!

**That's it! Setup complete in 2 minutes!** 🎉

📖 **For detailed automated setup guide, see:** [AUTOMATED_SETUP_GUIDE.md](AUTOMATED_SETUP_GUIDE.md)

---

### Manual Setup (Traditional Method)

### 1. XAMPP Setup

1. **Install XAMPP**
   - Download from: https://www.apachefriends.org/
   - Install and start Apache and MySQL

2. **Create Database - AUTOMATED** ⭐
   - Run `sync_api.bat` to deploy API files
   - Visit: http://localhost/fitness_tracker_api/setup.php
   - Click to automatically create database and tables!
   
   **OR Manual Method:**
   - Open phpMyAdmin: http://localhost/phpmyadmin
   - Create new database named: `fitness_tracker`
   - Import the SQL schema from `database_schema.sql`

3. **Place PHP Files - AUTOMATED** ⭐
   - Run `sync_api.bat` - automatically copies files to XAMPP
   
   **OR Manual Method:**
   - Copy the `php_api` folder to: `C:\xampp\htdocs\fitness_tracker_api`
   - Ensure folder structure:
     ```
     C:\xampp\htdocs\fitness_tracker_api\
     ├── config.php
     └── api\
         ├── register.php
         ├── login.php
         ├── workouts.php
         └── goals.php
     ```

4. **Test API**
   - Access: http://localhost/fitness_tracker_api/api/register.php
   - You should see a "Method not allowed" JSON response (expected for GET request)

### 2. Android App Setup

1. **Configure API URL**
   - Open: `RetrofitClient.kt`
   - Update `BASE_URL`:
     - For Android Emulator: `http://10.0.2.2/fitness_tracker_api/api/`
     - For Physical Device: `http://YOUR_LOCAL_IP/fitness_tracker_api/api/`
       (Find your IP: Run `ipconfig` in cmd, look for IPv4 Address)

2. **Sync Gradle**
   - Open project in Android Studio
   - Click "Sync Now" when prompted
   - Wait for dependencies to download

3. **Run App**
   - Connect device or start emulator
   - Click Run (Shift+F10)
   - App should launch to Login screen

---

## API ENDPOINTS

### Base URL
- Emulator: `http://10.0.2.2/fitness_tracker_api/api/`
- Device: `http://YOUR_IP/fitness_tracker_api/api/`

### Authentication Endpoints

#### POST /register.php
Register a new user

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123",
  "name": "John Doe",
  "age": 25,
  "weight": 75.5,
  "height": 175.0
}
```

**Response (201 Created):**
```json
{
  "status": "success",
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "email": "john@example.com",
    "name": "John Doe",
    "age": 25,
    "weight": 75.5,
    "height": 175.0
  }
}
```

#### POST /login.php
Login user

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "status": "success",
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "data": {
    "id": 1,
    "email": "john@example.com",
    "name": "John Doe",
    "age": 25,
    "weight": 75.5,
    "height": 175.0
  }
}
```

### Workout Endpoints (Requires Authentication)

#### GET /workouts.php?user_id={id}
Get all workouts for a user

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "workoutType": "running",
      "startTime": "2024-10-31 08:00:00",
      "endTime": "2024-10-31 08:30:00",
      "duration": 30,
      "caloriesBurned": 250.0,
      "distance": 5.0,
      "averagePace": 6.0
    }
  ]
}
```

#### POST /workouts.php
Create a new workout

**Headers:**
```
Authorization: Bearer {token}
```

**Request Body (Running):**
```json
{
  "userId": 1,
  "workoutType": "running",
  "startTime": "2024-10-31 08:00:00",
  "endTime": "2024-10-31 08:30:00",
  "duration": 30,
  "caloriesBurned": 250.0,
  "distance": 5.0,
  "averagePace": 6.0
}
```

**Request Body (Weightlifting):**
```json
{
  "userId": 1,
  "workoutType": "weightlifting",
  "startTime": "2024-10-31 10:00:00",
  "endTime": "2024-10-31 10:45:00",
  "duration": 45,
  "caloriesBurned": 180.0,
  "exerciseName": "Bench Press",
  "totalSets": 4,
  "totalReps": 40,
  "maxWeight": 80.0
}
```

**Request Body (Cycling):**
```json
{
  "userId": 1,
  "workoutType": "cycling",
  "startTime": "2024-10-31 14:00:00",
  "endTime": "2024-10-31 15:00:00",
  "duration": 60,
  "caloriesBurned": 400.0,
  "distance": 20.0,
  "averageSpeed": 20.0
}
```

#### DELETE /workouts.php?id={workout_id}
Delete a workout

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "status": "success",
  "message": "Workout deleted successfully"
}
```

### Goals Endpoints (Requires Authentication)

#### GET /goals.php?user_id={id}
Get all goals for a user

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "goalType": "weight_loss",
      "targetValue": 70.0,
      "currentValue": 75.0,
      "deadline": "2024-12-31",
      "achieved": false,
      "createdAt": "2024-10-31 12:00:00"
    }
  ]
}
```

#### POST /goals.php
Create a new goal

**Headers:**
```
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "userId": 1,
  "goalType": "weight_loss",
  "targetValue": 70.0,
  "currentValue": 75.0,
  "deadline": "2024-12-31"
}
```

---

## DATABASE SCHEMA

### Users Table
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    weight DOUBLE NOT NULL,
    height DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Workouts Table
```sql
CREATE TABLE workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    workout_type VARCHAR(50) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    duration INT NOT NULL,
    calories_burned DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### Running Workouts Table
```sql
CREATE TABLE running_workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT NOT NULL,
    distance DOUBLE NOT NULL,
    average_pace DOUBLE NOT NULL,
    route_data TEXT,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);
```

### Weightlifting Workouts Table
```sql
CREATE TABLE weightlifting_workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT NOT NULL,
    exercise_name VARCHAR(255) NOT NULL,
    total_sets INT NOT NULL,
    total_reps INT NOT NULL,
    max_weight DOUBLE NOT NULL,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);
```

### Cycling Workouts Table
```sql
CREATE TABLE cycling_workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT NOT NULL,
    distance DOUBLE NOT NULL,
    average_speed DOUBLE NOT NULL,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);
```

### Fitness Goals Table
```sql
CREATE TABLE fitness_goals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    goal_type VARCHAR(100) NOT NULL,
    target_value DOUBLE NOT NULL,
    current_value DOUBLE DEFAULT 0,
    deadline DATE NOT NULL,
    achieved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

---

## OBJECT-ORIENTED DESIGN

### Kotlin Inheritance Hierarchy

```kotlin
// Base abstract class
abstract class Workout(
    open val id: Int,
    open val userId: Int,
    open val workoutType: String,
    open val startTime: String,
    open val endTime: String?,
    open val duration: Int,
    open val caloriesBurned: Double
)

// Derived classes
class RunningWorkout(...) : Workout(...)
class WeightLiftingWorkout(...) : Workout(...)
class CyclingWorkout(...) : Workout(...)
```

### Key Design Patterns
- **Repository Pattern**: Separates data access logic
- **MVVM Pattern**: Separates UI from business logic
- **Singleton Pattern**: RetrofitClient instance
- **Observer Pattern**: LiveData observers

---

## APP FEATURES

### 1. User Authentication
- User registration with validation
- Secure login with JWT tokens
- Session management
- Logout functionality

### 2. Workout Tracking
- Three workout types: Running, Weightlifting, Cycling
- Type-specific data entry forms
- View workout history
- Delete workouts
- Display workout statistics

### 3. Goals Management
- Create fitness goals
- Track progress
- View all goals
- Progress visualization

### 4. Dashboard
- User profile display
- Quick action buttons
- Welcome message
- Stats overview

---

## SECURITY FEATURES

### Backend (PHP)
- **Password Hashing**: bcrypt algorithm
- **JWT Authentication**: Secure token-based auth
- **Input Sanitization**: Prevents XSS attacks
- **SQL Prepared Statements**: Prevents SQL injection
- **User Authorization**: Users can only access their own data

### Frontend (Kotlin)
- **Secure Storage**: SharedPreferences for tokens
- **Input Validation**: Client-side validation
- **HTTPS Ready**: Can be configured for SSL
- **Token Expiration**: Handles expired tokens

---

## TESTING

### Testing the API with Postman

1. **Register User**
   - POST http://localhost/fitness_tracker_api/api/register.php
   - Body: raw JSON with user data

2. **Login**
   - POST http://localhost/fitness_tracker_api/api/login.php
   - Save the token from response

3. **Create Workout**
   - POST http://localhost/fitness_tracker_api/api/workouts.php
   - Headers: Authorization: Bearer {token}
   - Body: workout data

4. **Get Workouts**
   - GET http://localhost/fitness_tracker_api/api/workouts.php?user_id=1
   - Headers: Authorization: Bearer {token}

---

## TROUBLESHOOTING

### Common Issues

1. **Cannot connect to API**
   - Check XAMPP Apache is running
   - Verify BASE_URL in RetrofitClient.kt
   - For physical device, use computer's IP address
   - Ensure firewall allows connections

2. **Database connection failed**
   - Check XAMPP MySQL is running
   - Verify database name is correct
   - Check config.php credentials

3. **401 Unauthorized**
   - Token expired, login again
   - Check Authorization header format
   - Verify token is being sent correctly

4. **CORS errors**
   - Check CORS headers in PHP files
   - Ensure Access-Control headers are set

5. **App crashes**
   - Check Logcat for errors
   - Verify all dependencies are synced
   - Check API responses are valid JSON

---

## PROJECT STRUCTURE

```
STANLEYGERSOMP00199276FITNESSTRACKER/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/stanleygersomp00199276fitnesstracker/
│   │   │   │   ├── adapters/
│   │   │   │   │   ├── WorkoutAdapter.kt
│   │   │   │   │   └── GoalAdapter.kt
│   │   │   │   ├── models/
│   │   │   │   │   ├── User.kt
│   │   │   │   │   ├── Workout.kt
│   │   │   │   │   ├── FitnessGoal.kt
│   │   │   │   │   └── ApiResponse.kt
│   │   │   │   ├── network/
│   │   │   │   │   ├── ApiService.kt
│   │   │   │   │   └── RetrofitClient.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── FitnessRepository.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── LoginActivity.kt
│   │   │   │   │   ├── RegisterActivity.kt
│   │   │   │   │   ├── DashboardActivity.kt
│   │   │   │   │   ├── AddWorkoutActivity.kt
│   │   │   │   │   ├── WorkoutHistoryActivity.kt
│   │   │   │   │   └── GoalsActivity.kt
│   │   │   │   ├── utils/
│   │   │   │   │   └── SessionManager.kt
│   │   │   │   └── viewmodel/
│   │   │   │       ├── AuthViewModel.kt
│   │   │   │       ├── WorkoutViewModel.kt
│   │   │   │       └── GoalViewModel.kt
│   │   │   ├── res/
│   │   │   │   └── layout/
│   │   │   │       ├── activity_login.xml
│   │   │   │       ├── activity_register.xml
│   │   │   │       ├── activity_dashboard.xml
│   │   │   │       ├── activity_add_workout.xml
│   │   │   │       ├── activity_workout_history.xml
│   │   │   │       ├── activity_goals.xml
│   │   │   │       ├── item_workout.xml
│   │   │   │       └── item_goal.xml
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
│   └── build.gradle.kts
├── php_api/
│   ├── config.php
│   └── api/
│       ├── register.php
│       ├── login.php
│       ├── workouts.php
│       └── goals.php
└── database_schema.sql
```

---

## FUTURE ENHANCEMENTS

1. **Offline Support**
   - Room Database for local caching
   - Sync when online

2. **Advanced Features**
   - Charts and graphs
   - Social sharing
   - Workout recommendations
   - Push notifications

3. **Additional Workout Types**
   - Swimming
   - Yoga
   - Custom workouts

4. **User Profile**
   - Edit profile
   - Profile picture
   - Achievement badges

---

## CREDITS

**Developer**: Stanley Gersom P00199276
**Project**: Fitness Tracker
**Technologies**: Kotlin, PHP, MySQL, XAMPP, Retrofit, JWT

---

## LICENSE

This project is for educational purposes.

