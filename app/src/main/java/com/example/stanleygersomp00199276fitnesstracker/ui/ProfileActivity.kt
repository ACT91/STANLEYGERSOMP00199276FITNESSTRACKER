package com.example.stanleygersomp00199276fitnesstracker.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityProfileBinding
import com.example.stanleygersomp00199276fitnesstracker.models.UpdateProfileRequest
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import com.example.stanleygersomp00199276fitnesstracker.utils.ErrorMessageHelper
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.AuthViewModel

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

    private lateinit var binding: ActivityProfileBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.gunmetal_green)

        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupToolbar()
        loadUserData()
        setupListeners()
        observeViewModel()

        // Optionally refresh from backend
        sessionManager.getAuthToken()?.let { token ->
            sessionManager.getUser()?.let { user ->
                viewModel.getProfile(token, user.id)
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        ToolbarUtils.tintToolbarIconsWhite(binding.toolbar)
    }

    private fun loadUserData() {
        val user = sessionManager.getUser()
        user?.let {
            binding.etName.setText(it.name)
            binding.etEmail.setText(it.email)
            binding.etAge.setText(it.age.toString())
            binding.etWeight.setText(it.weight.toString())
            binding.etHeight.setText(it.height.toString())
        }
    }

    private fun setupListeners() {
        binding.btnSaveProfile.setOnClickListener {
            Log.d(TAG, "Save Changes clicked")
            if (validateInput()) {
                saveProfile()
            } else {
                Toast.makeText(this, "Please fix the highlighted fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        val name = binding.etName.text.toString().trim()
        val age = binding.etAge.text.toString().trim()
        val weight = binding.etWeight.text.toString().trim()
        val height = binding.etHeight.text.toString().trim()

        if (name.isEmpty()) { binding.tilName.error = "Name is required"; isValid = false } else binding.tilName.error = null
        if (age.isEmpty()) { binding.tilAge.error = "Age is required"; isValid = false } else binding.tilAge.error = null
        if (weight.isEmpty()) { binding.tilWeight.error = "Weight is required"; isValid = false } else binding.tilWeight.error = null
        if (height.isEmpty()) { binding.tilHeight.error = "Height is required"; isValid = false } else binding.tilHeight.error = null

        val currentPassword = binding.etCurrentPassword.text.toString()
        val newPassword = binding.etNewPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (currentPassword.isNotEmpty() || newPassword.isNotEmpty() || confirmPassword.isNotEmpty()) {
            if (currentPassword.isEmpty()) { binding.tilCurrentPassword.error = "Current password is required"; isValid = false } else binding.tilCurrentPassword.error = null
            if (newPassword.isEmpty()) { binding.tilNewPassword.error = "New password is required"; isValid = false }
            else if (newPassword.length < 6) { binding.tilNewPassword.error = "Password must be at least 6 characters"; isValid = false } else binding.tilNewPassword.error = null
            if (confirmPassword != newPassword) { binding.tilConfirmPassword.error = "Passwords do not match"; isValid = false } else binding.tilConfirmPassword.error = null
        }
        return isValid
    }

    private fun saveProfile() {
        val user = sessionManager.getUser()
        val token = sessionManager.getAuthToken()
        if (user == null || token == null) {
            Log.w(TAG, "Missing user/token. user=${user != null}, token=${token != null}")
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show()
            return
        }

        val name = binding.etName.text.toString().trim()
        val age = binding.etAge.text.toString().toInt()
        val weight = binding.etWeight.text.toString().toDouble()
        val height = binding.etHeight.text.toString().toDouble()

        val req = UpdateProfileRequest(
            userId = user.id,
            name = name,
            age = age,
            weight = weight,
            height = height,
            currentPassword = binding.etCurrentPassword.text.toString().ifEmpty { null },
            newPassword = binding.etNewPassword.text.toString().ifEmpty { null }
        )

        Log.d(TAG, "Calling updateProfile for userId=${user.id}")
        viewModel.updateProfile(token, req)
    }

    private fun observeViewModel() {
        viewModel.profileResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    result.data?.let { updated ->
                        sessionManager.saveUser(updated)
                        loadUserData()
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.message ?: "Failed to load profile", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.updateProfileResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    result.data?.let { updated ->
                        sessionManager.saveUser(updated)
                        loadUserData()
                    }
                    // Clear password fields
                    binding.etCurrentPassword.setText("")
                    binding.etNewPassword.setText("")
                    binding.etConfirmPassword.setText("")
                    Toast.makeText(this, ErrorMessageHelper.getSuccessMessage("update_profile"), Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.message ?: "Failed to update profile", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSaveProfile.isEnabled = !isLoading
    }
}
