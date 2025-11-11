# API ENDPOINT TESTING GUIDE

## Postman Collection for Testing

### 1. REGISTER USER
```
Method: POST
URL: http://localhost/fitness_tracker_api/api/register.php
Headers:
  Content-Type: application/json

Body (raw JSON):
{
  "email": "john.doe@example.com",
  "password": "password123",
  "name": "John Doe",
  "age": 28,
  "weight": 75.5,
  "height": 178.0
}

Expected Response (201):
{
  "status": "success",
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "email": "john.doe@example.com",
    "name": "John Doe",
    "age": 28,
    "weight": 75.5,
    "height": 178
  }
}
```

### 2. LOGIN
```
Method: POST
URL: http://localhost/fitness_tracker_api/api/login.php
Headers:
  Content-Type: application/json

Body (raw JSON):
{
  "email": "john.doe@example.com",
  "password": "password123"
}

Expected Response (200):
{
  "status": "success",
  "message": "Login successful",
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "data": {
    "id": 1,
    "email": "john.doe@example.com",
    "name": "John Doe",
    "age": 28,
    "weight": 75.5,
    "height": 178
  }
}

⚠️ SAVE THE TOKEN - You'll need it for authenticated requests!
```

### 3. CREATE RUNNING WORKOUT
```
Method: POST
URL: http://localhost/fitness_tracker_api/api/workouts.php
Headers:
  Content-Type: application/json
  Authorization: Bearer YOUR_TOKEN_HERE

Body (raw JSON):
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

Expected Response (201):
{
  "status": "success",
  "message": "Workout created successfully",
  "data": {
    "id": 1
  }
}
```

### 4. CREATE WEIGHTLIFTING WORKOUT
```
Method: POST
URL: http://localhost/fitness_tracker_api/api/workouts.php
Headers:
  Content-Type: application/json
  Authorization: Bearer YOUR_TOKEN_HERE

Body (raw JSON):
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

Expected Response (201):
{
  "status": "success",
  "message": "Workout created successfully",
  "data": {
    "id": 2
  }
}
```

### 5. CREATE CYCLING WORKOUT
```
Method: POST
URL: http://localhost/fitness_tracker_api/api/workouts.php
Headers:
  Content-Type: application/json
  Authorization: Bearer YOUR_TOKEN_HERE

Body (raw JSON):
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

Expected Response (201):
{
  "status": "success",
  "message": "Workout created successfully",
  "data": {
    "id": 3
  }
}
```

### 6. GET ALL WORKOUTS
```
Method: GET
URL: http://localhost/fitness_tracker_api/api/workouts.php?user_id=1
Headers:
  Authorization: Bearer YOUR_TOKEN_HERE

Expected Response (200):
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
      "caloriesBurned": 250,
      "distance": 5,
      "averagePace": 6,
      "routeData": null
    },
    {
      "id": 2,
      "userId": 1,
      "workoutType": "weightlifting",
      "startTime": "2024-10-31 10:00:00",
      "endTime": "2024-10-31 10:45:00",
      "duration": 45,
      "caloriesBurned": 180,
      "exerciseName": "Bench Press",
      "totalSets": 4,
      "totalReps": 40,
      "maxWeight": 80
    },
    {
      "id": 3,
      "userId": 1,
      "workoutType": "cycling",
      "startTime": "2024-10-31 14:00:00",
      "endTime": "2024-10-31 15:00:00",
      "duration": 60,
      "caloriesBurned": 400,
      "distance": 20,
      "averageSpeed": 20
    }
  ]
}
```

### 7. DELETE WORKOUT
```
Method: DELETE
URL: http://localhost/fitness_tracker_api/api/workouts.php?id=1
Headers:
  Authorization: Bearer YOUR_TOKEN_HERE

Expected Response (200):
{
  "status": "success",
  "message": "Workout deleted successfully"
}
```

### 8. CREATE GOAL
```
Method: POST
URL: http://localhost/fitness_tracker_api/api/goals.php
Headers:
  Content-Type: application/json
  Authorization: Bearer YOUR_TOKEN_HERE

Body (raw JSON):
{
  "userId": 1,
  "goalType": "weight_loss",
  "targetValue": 70.0,
  "currentValue": 75.5,
  "deadline": "2024-12-31"
}

Expected Response (201):
{
  "status": "success",
  "message": "Goal created successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "goalType": "weight_loss",
    "targetValue": 70,
    "currentValue": 75.5,
    "deadline": "2024-12-31",
    "achieved": false
  }
}
```

### 9. GET ALL GOALS
```
Method: GET
URL: http://localhost/fitness_tracker_api/api/goals.php?user_id=1
Headers:
  Authorization: Bearer YOUR_TOKEN_HERE

Expected Response (200):
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "goalType": "weight_loss",
      "targetValue": 70,
      "currentValue": 75.5,
      "deadline": "2024-12-31",
      "achieved": false,
      "createdAt": "2024-10-31 12:00:00"
    }
  ]
}
```

### 10. UPDATE GOAL PROGRESS
```
Method: PUT
URL: http://localhost/fitness_tracker_api/api/goals.php?id=1
Headers:
  Content-Type: application/json
  Authorization: Bearer YOUR_TOKEN_HERE

Body (raw JSON):
{
  "currentValue": 72.0,
  "achieved": false
}

Expected Response (200):
{
  "status": "success",
  "message": "Goal updated successfully"
}
```

---

## cURL Commands for Testing

### Register User
```bash
curl -X POST http://localhost/fitness_tracker_api/api/register.php \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"test@example.com\",\"password\":\"password123\",\"name\":\"Test User\",\"age\":25,\"weight\":70,\"height\":175}"
```

### Login
```bash
curl -X POST http://localhost/fitness_tracker_api/api/login.php \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"test@example.com\",\"password\":\"password123\"}"
```

### Create Workout (Replace YOUR_TOKEN)
```bash
curl -X POST http://localhost/fitness_tracker_api/api/workouts.php \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d "{\"userId\":1,\"workoutType\":\"running\",\"startTime\":\"2024-10-31 08:00:00\",\"endTime\":\"2024-10-31 08:30:00\",\"duration\":30,\"caloriesBurned\":250,\"distance\":5,\"averagePace\":6}"
```

### Get Workouts (Replace YOUR_TOKEN)
```bash
curl -X GET "http://localhost/fitness_tracker_api/api/workouts.php?user_id=1" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## Error Response Examples

### 400 Bad Request - Missing Fields
```json
{
  "status": "error",
  "message": "Missing required field: email"
}
```

### 401 Unauthorized - Invalid Token
```json
{
  "status": "error",
  "message": "Unauthorized - Invalid or expired token"
}
```

### 403 Forbidden - Accessing Other User's Data
```json
{
  "status": "error",
  "message": "Forbidden - You can only access your own workouts"
}
```

### 404 Not Found
```json
{
  "status": "error",
  "message": "Workout not found"
}
```

### 409 Conflict - Email Already Exists
```json
{
  "status": "error",
  "message": "Email already registered"
}
```

### 500 Internal Server Error
```json
{
  "status": "error",
  "message": "Database connection failed"
}
```

---

## Testing Workflow

1. **Register a new user** → Get user ID
2. **Login with that user** → Get authentication token
3. **Create workouts** using the token
4. **Retrieve workouts** to verify creation
5. **Create goals** for the user
6. **Update goal progress**
7. **Delete a workout** to test deletion

---

## Validation Test Cases

### Email Validation
- ❌ Invalid: `notanemail`
- ❌ Invalid: `test@`
- ✅ Valid: `test@example.com`

### Password Validation
- ❌ Too short: `12345`
- ✅ Valid: `password123` (6+ chars)

### Numeric Fields
- Age: Must be positive integer
- Weight: Must be positive number
- Height: Must be positive number
- Duration: Must be positive integer

### Authorization
- ❌ No token: 401 Unauthorized
- ❌ Invalid token: 401 Unauthorized
- ❌ Expired token: 401 Unauthorized
- ✅ Valid token: Request succeeds

---

## Database Verification

After API calls, verify in phpMyAdmin:

1. **After Registration**
   - Check `users` table
   - Password should be hashed (not plain text)

2. **After Creating Running Workout**
   - Check `workouts` table (1 row)
   - Check `running_workouts` table (1 row)

3. **After Creating Weightlifting Workout**
   - Check `workouts` table (2 rows)
   - Check `weightlifting_workouts` table (1 row)

4. **After Deleting Workout**
   - Check `workouts` table (1 row removed)
   - Check detail table (corresponding row auto-deleted via CASCADE)

---

## Security Testing

### Test SQL Injection Prevention
Try registering with:
```json
{
  "email": "test'; DROP TABLE users; --@example.com",
  "password": "password123",
  "name": "Test",
  "age": 25,
  "weight": 70,
  "height": 175
}
```
✅ Should be safely escaped and fail with invalid email format

### Test XSS Prevention
Try registering with:
```json
{
  "name": "<script>alert('XSS')</script>",
  ...
}
```
✅ Should be sanitized and stored as plain text

### Test Authorization
1. Login as User A (get token A)
2. Try to access User B's workouts with token A
✅ Should return 403 Forbidden

---

## Performance Testing

### Benchmark Response Times
- Registration: < 500ms
- Login: < 300ms
- Create Workout: < 200ms
- Get Workouts: < 300ms
- Delete Workout: < 200ms

### Load Testing
Test with multiple concurrent requests:
```bash
# Using Apache Bench (if installed)
ab -n 100 -c 10 http://localhost/fitness_tracker_api/api/login.php
```

---

## Integration Testing Checklist

- ✅ User can register
- ✅ User can login
- ✅ User receives valid JWT token
- ✅ Token works for authenticated requests
- ✅ User can create all workout types
- ✅ Workout details are saved correctly
- ✅ User can retrieve their workouts
- ✅ User cannot access other users' data
- ✅ User can delete their workouts
- ✅ Cascade deletion works (detail tables)
- ✅ User can create goals
- ✅ User can update goals
- ✅ Input validation works
- ✅ Error messages are clear
- ✅ SQL injection is prevented
- ✅ XSS is prevented
- ✅ Passwords are hashed

---

Happy Testing! 🧪

