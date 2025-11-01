package com.example.stanleygersomp00199276fitnesstracker.models

data class ApiResponse<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null,
    val token: String? = null
)

