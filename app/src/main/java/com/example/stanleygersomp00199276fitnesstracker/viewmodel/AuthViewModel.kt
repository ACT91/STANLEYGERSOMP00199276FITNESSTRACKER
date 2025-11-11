package com.example.stanleygersomp00199276fitnesstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stanleygersomp00199276fitnesstracker.models.*
import com.example.stanleygersomp00199276fitnesstracker.repository.FitnessRepository
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = FitnessRepository()

    private val _registerResult = MutableLiveData<Resource<User>>()
    val registerResult: LiveData<Resource<User>> = _registerResult

    private val _loginResult = MutableLiveData<Resource<Pair<User, String>>>()
    val loginResult: LiveData<Resource<Pair<User, String>>> = _loginResult

    // New: profile endpoints
    private val _profileResult = MutableLiveData<Resource<User>>()
    val profileResult: LiveData<Resource<User>> = _profileResult

    private val _updateProfileResult = MutableLiveData<Resource<User>>()
    val updateProfileResult: LiveData<Resource<User>> = _updateProfileResult

    fun register(
        email: String,
        password: String,
        name: String,
        age: Int
    ) {
        viewModelScope.launch {
            _registerResult.value = Resource.Loading()
            val userRegistration = UserRegistration(email, password, name, age)
            _registerResult.value = repository.register(userRegistration)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Resource.Loading()
            val credentials = UserLogin(email, password)
            _loginResult.value = repository.login(credentials)
        }
    }

    fun getProfile(token: String, userId: Int) {
        viewModelScope.launch {
            _profileResult.value = Resource.Loading()
            _profileResult.value = repository.getProfile(token, userId)
        }
    }

    fun updateProfile(token: String, request: UpdateProfileRequest) {
        viewModelScope.launch {
            _updateProfileResult.value = Resource.Loading()
            _updateProfileResult.value = repository.updateProfile(token, request)
        }
    }
}
