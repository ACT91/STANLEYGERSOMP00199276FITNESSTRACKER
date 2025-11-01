package com.example.stanleygersomp00199276fitnesstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stanleygersomp00199276fitnesstracker.models.WorkoutData
import com.example.stanleygersomp00199276fitnesstracker.repository.FitnessRepository
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import kotlinx.coroutines.launch

class WorkoutViewModel : ViewModel() {

    private val repository = FitnessRepository()

    private val _workouts = MutableLiveData<Resource<List<WorkoutData>>>()
    val workouts: LiveData<Resource<List<WorkoutData>>> = _workouts

    private val _createWorkoutResult = MutableLiveData<Resource<WorkoutData>>()
    val createWorkoutResult: LiveData<Resource<WorkoutData>> = _createWorkoutResult

    private val _deleteWorkoutResult = MutableLiveData<Resource<Boolean>>()
    val deleteWorkoutResult: LiveData<Resource<Boolean>> = _deleteWorkoutResult

    fun getUserWorkouts(token: String, userId: Int) {
        viewModelScope.launch {
            _workouts.value = Resource.Loading()
            _workouts.value = repository.getUserWorkouts(token, userId)
        }
    }

    fun createWorkout(token: String, workout: WorkoutData) {
        viewModelScope.launch {
            _createWorkoutResult.value = Resource.Loading()
            _createWorkoutResult.value = repository.createWorkout(token, workout)
        }
    }

    fun deleteWorkout(token: String, workoutId: Int) {
        viewModelScope.launch {
            _deleteWorkoutResult.value = Resource.Loading()
            _deleteWorkoutResult.value = repository.deleteWorkout(token, workoutId)
        }
    }
}

