package com.example.stanleygersomp00199276fitnesstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stanleygersomp00199276fitnesstracker.models.Achievement
import com.example.stanleygersomp00199276fitnesstracker.repository.FitnessRepository
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import kotlinx.coroutines.launch

class AchievementViewModel : ViewModel() {

    private val repository = FitnessRepository()

    private val _achievements = MutableLiveData<Resource<List<Achievement>>>()
    val achievements: LiveData<Resource<List<Achievement>>> = _achievements

    fun getUserAchievements(token: String, userId: Int) {
        viewModelScope.launch {
            _achievements.value = Resource.Loading()
            _achievements.value = repository.getUserAchievements(token, userId)
        }
    }
}

