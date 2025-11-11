package com.example.stanleygersomp00199276fitnesstracker.repository

import com.example.stanleygersomp00199276fitnesstracker.models.*
import com.example.stanleygersomp00199276fitnesstracker.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

class FitnessRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun register(userRegistration: UserRegistration): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(userRegistration)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success" && apiResponse.data != null) {
                        Resource.Success(apiResponse.data)
                    } else {
                        Resource.Error(apiResponse?.message ?: "Registration failed")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun login(credentials: UserLogin): Resource<Pair<User, String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(credentials)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success" && apiResponse.data != null) {
                        val token = apiResponse.token ?: ""
                        Resource.Success(Pair(apiResponse.data, token))
                    } else {
                        Resource.Error(apiResponse?.message ?: "Login failed")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun createWorkout(token: String, workout: WorkoutData): Resource<WorkoutData> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.createWorkout("Bearer $token", workout)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success" && apiResponse.data != null) {
                        Resource.Success(apiResponse.data)
                    } else {
                        Resource.Error(apiResponse?.message ?: "Failed to create workout")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun getUserWorkouts(token: String, userId: Int): Resource<List<WorkoutData>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserWorkouts("Bearer $token", userId)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success") {
                        Resource.Success(apiResponse.data ?: emptyList())
                    } else {
                        Resource.Error(apiResponse?.message ?: "Failed to fetch workouts")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun deleteWorkout(token: String, workoutId: Int): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteWorkout("Bearer $token", workoutId)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success") {
                        Resource.Success(true)
                    } else {
                        Resource.Error(apiResponse?.message ?: "Failed to delete workout")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun getUserGoals(token: String, userId: Int): Resource<List<FitnessGoal>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserGoals("Bearer $token", userId)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success") {
                        Resource.Success(apiResponse.data ?: emptyList())
                    } else {
                        Resource.Error(apiResponse?.message ?: "Failed to fetch goals")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun createGoal(token: String, goal: FitnessGoal): Resource<FitnessGoal> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.createGoal("Bearer $token", goal)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success" && apiResponse.data != null) {
                        Resource.Success(apiResponse.data)
                    } else {
                        Resource.Error(apiResponse?.message ?: "Failed to create goal")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun getProfile(token: String, userId: Int): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val resp = apiService.getProfile("Bearer $token", userId)
                if (resp.isSuccessful) {
                    val body = resp.body()
                    if (body?.status == "success" && body.data != null) {
                        Resource.Success(body.data)
                    } else Resource.Error(body?.message ?: "Failed to fetch profile")
                } else Resource.Error("Server error: ${resp.code()}")
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun updateProfile(token: String, request: UpdateProfileRequest): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val resp = apiService.updateProfile("Bearer $token", request)
                if (resp.isSuccessful) {
                    val body = resp.body()
                    if (body?.status == "success" && body.data != null) {
                        Resource.Success(body.data)
                    } else Resource.Error(body?.message ?: "Failed to update profile")
                } else Resource.Error("Server error: ${resp.code()}")
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun deleteGoal(token: String, goalId: Int): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteGoal("Bearer $token", goalId)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success") {
                        Resource.Success(true)
                    } else {
                        Resource.Error(apiResponse?.message ?: "Failed to delete goal")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun updateGoal(token: String, goalId: Int, goal: FitnessGoal): Resource<FitnessGoal> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateGoal("Bearer $token", goalId, goal)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success" && apiResponse.data != null) {
                        Resource.Success(apiResponse.data)
                    } else {
                        Resource.Error(apiResponse?.message ?: "Failed to update goal")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun getUserAchievements(token: String, userId: Int): Resource<List<Achievement>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserAchievements("Bearer $token", userId)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "success") {
                        Resource.Success(apiResponse.data ?: emptyList())
                    } else {
                        Resource.Error(apiResponse?.message ?: "Failed to fetch achievements")
                    }
                } else {
                    Resource.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Network error: ${e.message}")
            }
        }
    }
}
