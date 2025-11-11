package com.example.stanleygersomp00199276fitnesstracker.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityRegisterBinding
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import com.example.stanleygersomp00199276fitnesstracker.utils.ErrorMessageHelper
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val age = binding.etAge.text.toString().trim()

            if (validateInput(name, email, password, age)) {
                viewModel.register(
                    email = email,
                    password = password,
                    name = name,
                    age = age.toInt()
                )
            }
        }

        binding.tvLoginPrompt.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        age: String
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.tilName.error = "Name is required"
            isValid = false
        } else {
            binding.tilName.error = null
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        if (password.isEmpty() || password.length < 6) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        if (age.isEmpty()) {
            binding.tilAge.error = "Age is required"
            isValid = false
        } else {
            binding.tilAge.error = null
        }

        return isValid
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    val message = ErrorMessageHelper.getSuccessMessage("register")
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    finish()
                }
                is Resource.Error -> {
                    showLoading(false)
                    val userMessage = ErrorMessageHelper.getUserFriendlyMessage(result.message)
                    Toast.makeText(this, userMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !isLoading
    }
}

