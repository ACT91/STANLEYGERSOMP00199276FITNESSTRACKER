package com.example.stanleygersomp00199276fitnesstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stanleygersomp00199276fitnesstracker.models.FitnessGoal
import com.example.stanleygersomp00199276fitnesstracker.repository.FitnessRepository
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import kotlinx.coroutines.launch

class GoalViewModel : ViewModel() {

    private val repository = FitnessRepository()

    private val _goals = MutableLiveData<Resource<List<FitnessGoal>>>()
    val goals: LiveData<Resource<List<FitnessGoal>>> = _goals

    private val _createGoalResult = MutableLiveData<Resource<FitnessGoal>>()
    val createGoalResult: LiveData<Resource<FitnessGoal>> = _createGoalResult

    fun getUserGoals(token: String, userId: Int) {
        viewModelScope.launch {
            _goals.value = Resource.Loading()
            _goals.value = repository.getUserGoals(token, userId)
        }
    }

    fun createGoal(token: String, goal: FitnessGoal) {
        viewModelScope.launch {
            _createGoalResult.value = Resource.Loading()
            _createGoalResult.value = repository.createGoal(token, goal)
        }
    }
}

