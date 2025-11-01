package com.example.stanleygersomp00199276fitnesstracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityDashboardBinding
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin()
            return
        }

        // Inflate menu with profile icon and handle clicks
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.action_about -> {
                    // Navigate to AboutActivity when About menu item is selected
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }
                else -> false
            }
        }

        setupUserProfile()
        setupListeners()
    }

    private fun setupUserProfile() {
        val user = sessionManager.getUser()
        user?.let {
            binding.tvUserName.text = it.name
            binding.tvUserStats.text = "Age: ${it.age} | Weight: ${it.weight} kg | Height: ${it.height} cm"
        }
    }

    private fun setupListeners() {
        // FloatingActionButton for quick workout add
        binding.fabAddWorkout.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnAddWorkout.setOnClickListener {
            startActivity(Intent(this, AddWorkoutActivity::class.java))
        }

        binding.btnViewWorkouts.setOnClickListener {
            startActivity(Intent(this, WorkoutHistoryActivity::class.java))
        }

        binding.btnViewGoals.setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                sessionManager.logout()
                navigateToLogin()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
