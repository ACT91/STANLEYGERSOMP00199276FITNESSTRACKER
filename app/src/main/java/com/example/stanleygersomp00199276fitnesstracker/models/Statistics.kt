package com.example.stanleygersomp00199276fitnesstracker.models

import java.time.LocalDate

// Data models for analytics and insights
data class Statistics(
    val totalWorkouts: Int = 0,
    val totalDurationMinutes: Int = 0,
    val totalCalories: Double = 0.0,
    val totalDistanceKm: Double = 0.0,
    val averageDurationMinutes: Double = 0.0,
    val averageCalories: Double = 0.0,
    val workoutsByType: Map<String, Int> = emptyMap(),
    val weeklyTrends: Map<String, Double> = emptyMap(), // e.g. week label -> distance or duration
    val currentStreakDays: Int = 0,
    val longestStreakDays: Int = 0
)

// Insight and achievement models used by adapters
data class Insight(
    val id: String,
    val title: String,
    val message: String
)

data class Achievement(
    val id: Int = 0,
    val userId: Int = 0,
    val goalId: Int = 0,
    val title: String,
    val description: String,
    val achievedAt: String? = null,
    val unlocked: Boolean = false
)

