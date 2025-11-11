package com.example.stanleygersomp00199276fitnesstracker.models

data class User(
    val id: Int = 0,
    val email: String,
    val name: String,
    val age: Int,
    val weight: Double? = null,
    val height: Double? = null,
    val createdAt: String? = null
)

data class UserRegistration(
    val email: String,
    val password: String,
    val name: String,
    val age: Int
)

data class UserLogin(
    val email: String,
    val password: String
)

// New request model for updating profile
data class UpdateProfileRequest(
    val userId: Int,
    val name: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val currentPassword: String? = null,
    val newPassword: String? = null
)
