# 📊 Database Tables - Complete List

## Total Tables: **7 Tables**

Your `fitness_tracker` database should contain exactly **7 tables**:

---

## 📋 Complete Table List

### 1. **users** 
**Purpose:** User accounts and profiles
- Stores: id, email, password_hash, name, age, weight, height, created_at
- Primary Key: id

### 2. **fitness_goals**
**Purpose:** User fitness goals
- Stores: id, user_id, goal_type, target_value, current_value, deadline, achieved, created_at
- Primary Key: id
- Foreign Key: user_id → users.id

### 3. **workouts**
**Purpose:** Main workout records (linked to goals)
- Stores: id, user_id, goal_id, workout_type, start_time, end_time, duration, calories_burned, created_at
- Primary Key: id
- Foreign Keys: 
  - user_id → users.id
  - goal_id → fitness_goals.id

### 4. **running_workouts**
**Purpose:** Running-specific workout data
- Stores: id, workout_id, distance, average_pace, route_data
- Primary Key: id
- Foreign Key: workout_id → workouts.id

### 5. **weightlifting_workouts**
**Purpose:** Weightlifting-specific workout data
- Stores: id, workout_id, exercise_name, total_sets, total_reps, max_weight
- Primary Key: id
- Foreign Key: workout_id → workouts.id

### 6. **cycling_workouts**
**Purpose:** Cycling-specific workout data
- Stores: id, workout_id, distance, average_speed
- Primary Key: id
- Foreign Key: workout_id → workouts.id

### 7. **achievements**
**Purpose:** Completed goals as achievements
- Stores: id, user_id, goal_id, title, description, achieved_at
- Primary Key: id
- Foreign Keys:
  - user_id → users.id
  - goal_id → fitness_goals.id

---

## 🔗 Table Relationships

```
users (1)
  ├─→ workouts (many) ──→ goal_id links to fitness_goals
  │     ├─→ running_workouts (1)
  │     ├─→ weightlifting_workouts (1)
  │     └─→ cycling_workouts (1)
  ├─→ fitness_goals (many)
  └─→ achievements (many)
```

---

## 📑 Indexes Created

4 indexes for performance:
1. `idx_workouts_user_id` on workouts(user_id)
2. `idx_workouts_goal_id` on workouts(goal_id)
3. `idx_goals_user_id` on fitness_goals(user_id)
4. `idx_achievements_user_id` on achievements(user_id)

---

## ✅ Verification

After running setup, you should see all 7 tables in phpMyAdmin:

```
fitness_tracker
├── users
├── fitness_goals
├── workouts
├── running_workouts
├── weightlifting_workouts
├── cycling_workouts
└── achievements
```

**Total: 7 tables + 4 indexes**

---

## 🧪 Quick Check

To verify all tables exist, run this SQL:

```sql
USE fitness_tracker;
SHOW TABLES;
```

**Expected output:**
```
+---------------------------+
| Tables_in_fitness_tracker |
+---------------------------+
| achievements              |
| cycling_workouts          |
| fitness_goals             |
| running_workouts          |
| users                     |
| weightlifting_workouts    |
| workouts                  |
+---------------------------+
7 rows in set
```

---

## 📊 Table Size Estimates

After initial setup (empty database):
- All tables: 0 rows
- Total size: ~100 KB (structure only)

After using the app:
- users: ~1-1000 rows
- fitness_goals: ~5-50 per user
- workouts: ~100-10000 rows
- activity tables: 1 row per matching workout
- achievements: 1 row per completed goal

---

## Summary

**Answer: Your database should have exactly 7 tables.**

All 7 tables are created automatically when you run:
- `setup_database.bat`, OR
- `http://localhost/fitness_tracker_api/setup.php`

If you see any number other than 7 tables, something went wrong during setup!

