package com.example.stanleygersomp00199276fitnesstracker.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityStatisticsBinding
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import com.example.stanleygersomp00199276fitnesstracker.utils.FitnessAnalytics
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.WorkoutViewModel

class StatisticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.gunmetal_green)

        sessionManager = SessionManager(this)

        setupToolbar()
        loadStatistics()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        // No menu for statistics page
    }

    private fun loadStatistics() {
        val token = sessionManager.getAuthToken()
        val userId = sessionManager.getUser()?.id

        if (!token.isNullOrEmpty() && userId != null) {
            workoutViewModel.getUserWorkouts(token, userId)
        } else {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun observeViewModel() {
        workoutViewModel.workouts.observe(this) { res ->
            when (res) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.statsContainer.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.statsContainer.visibility = View.VISIBLE

                    val workouts = res.data ?: emptyList()
                    val stats = FitnessAnalytics.computeStatistics(workouts)

                    // Display statistics
                    binding.tvTotalWorkouts.text = stats.totalWorkouts.toString()
                    binding.tvTotalDuration.text = "${stats.totalDurationMinutes} min"
                    binding.tvTotalCalories.text = "${stats.totalCalories} kcal"
                    binding.tvTotalDistance.text = "${stats.totalDistanceKm} km"
                    binding.tvAverageDuration.text = "${stats.averageDurationMinutes} min"
                    binding.tvAverageCalories.text = "${stats.averageCalories} kcal"
                    binding.tvCurrentStreak.text = "${stats.currentStreakDays} days"
                    binding.tvLongestStreak.text = "${stats.longestStreakDays} days"

                    // Display workout breakdown
                    val breakdown = StringBuilder()
                    stats.workoutsByType.forEach { (type, count) ->
                        breakdown.append("${type.capitalize()}: $count workouts\n")
                    }
                    binding.tvWorkoutBreakdown.text = if (breakdown.isEmpty()) "No workouts yet" else breakdown.toString()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
