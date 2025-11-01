package com.example.stanleygersomp00199276fitnesstracker.models

data class FitnessGoal(
    val id: Int = 0,
    val userId: Int,
    val goalType: String, // e.g., "weight_loss", "distance", "workout_count"
    val targetValue: Double,
    val currentValue: Double = 0.0,
    val deadline: String,
    val achieved: Boolean = false,
    val createdAt: String? = null
)

