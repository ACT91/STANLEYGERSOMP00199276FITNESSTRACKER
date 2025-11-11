package com.example.stanleygersomp00199276fitnesstracker.models

// Base abstract workout class
abstract class Workout(
    open val id: Int = 0,
    open val userId: Int,
    open val workoutType: String,
    open val startTime: String,
    open val endTime: String? = null,
    open val duration: Int = 0, // in minutes
    open val caloriesBurned: Double = 0.0
)

data class RunningWorkout(
    override val id: Int = 0,
    override val userId: Int,
    override val startTime: String,
    override val endTime: String? = null,
    override val duration: Int = 0,
    override val caloriesBurned: Double = 0.0,
    val distance: Double = 0.0, // in kilometers
    val averagePace: Double = 0.0, // minutes per km
    val routeData: String? = null
) : Workout(id, userId, "running", startTime, endTime, duration, caloriesBurned)

data class WeightLiftingWorkout(
    override val id: Int = 0,
    override val userId: Int,
    override val startTime: String,
    override val endTime: String? = null,
    override val duration: Int = 0,
    override val caloriesBurned: Double = 0.0,
    val exerciseName: String,
    val totalSets: Int = 0,
    val totalReps: Int = 0,
    val maxWeight: Double = 0.0 // in kg
) : Workout(id, userId, "weightlifting", startTime, endTime, duration, caloriesBurned)

data class CyclingWorkout(
    override val id: Int = 0,
    override val userId: Int,
    override val startTime: String,
    override val endTime: String? = null,
    override val duration: Int = 0,
    override val caloriesBurned: Double = 0.0,
    val distance: Double = 0.0, // in kilometers
    val averageSpeed: Double = 0.0 // km/h
) : Workout(id, userId, "cycling", startTime, endTime, duration, caloriesBurned)

// Generic workout data for API responses
data class WorkoutData(
    val id: Int = 0,
    val userId: Int,
    val goalId: Int? = null,
    val workoutType: String,
    val startTime: String,
    val endTime: String? = null,
    val duration: Int = 0,
    val caloriesBurned: Double = 0.0,
    // Running specific
    val distance: Double? = null,
    val averagePace: Double? = null,
    val routeData: String? = null,
    // Weightlifting specific
    val exerciseName: String? = null,
    val totalSets: Int? = null,
    val totalReps: Int? = null,
    val maxWeight: Double? = null,
    // Cycling specific
    val averageSpeed: Double? = null
)

