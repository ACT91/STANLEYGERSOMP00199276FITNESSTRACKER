package com.example.stanleygersomp00199276fitnesstracker.network

import com.example.stanleygersomp00199276fitnesstracker.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("register.php")
    suspend fun register(@Body user: UserRegistration): Response<ApiResponse<User>>

    @POST("login.php")
    suspend fun login(@Body credentials: UserLogin): Response<ApiResponse<User>>

    @POST("workouts.php")
    suspend fun createWorkout(
        @Header("Authorization") token: String,
        @Body workout: WorkoutData
    ): Response<ApiResponse<WorkoutData>>

    @GET("workouts.php")
    suspend fun getUserWorkouts(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int
    ): Response<ApiResponse<List<WorkoutData>>>

    @PUT("workouts.php")
    suspend fun updateWorkout(
        @Header("Authorization") token: String,
        @Query("id") workoutId: Int,
        @Body workout: WorkoutData
    ): Response<ApiResponse<WorkoutData>>

    @DELETE("workouts.php")
    suspend fun deleteWorkout(
        @Header("Authorization") token: String,
        @Query("id") workoutId: Int
    ): Response<ApiResponse<Any>>

    @GET("goals.php")
    suspend fun getUserGoals(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int
    ): Response<ApiResponse<List<FitnessGoal>>>

    @POST("goals.php")
    suspend fun createGoal(
        @Header("Authorization") token: String,
        @Body goal: FitnessGoal
    ): Response<ApiResponse<FitnessGoal>>

    @PUT("goals.php")
    suspend fun updateGoal(
        @Header("Authorization") token: String,
        @Query("id") goalId: Int,
        @Body goal: FitnessGoal
    ): Response<ApiResponse<FitnessGoal>>

    // New Profile endpoints
    @GET("get_profile.php")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Query("user_id") userId: Int
    ): Response<ApiResponse<User>>

    @PUT("update_profile.php")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<User>>
}
