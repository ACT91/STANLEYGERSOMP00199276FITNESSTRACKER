package com.example.stanleygersomp00199276fitnesstracker.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityAddWorkoutBinding
import com.example.stanleygersomp00199276fitnesstracker.models.WorkoutData
import com.example.stanleygersomp00199276fitnesstracker.models.FitnessGoal
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import com.example.stanleygersomp00199276fitnesstracker.utils.ErrorMessageHelper
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.WorkoutViewModel
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.GoalViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddWorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddWorkoutBinding
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val goalViewModel: GoalViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private var selectedWorkoutType = "running"
    private var userGoals = listOf<FitnessGoal>()
    private var selectedGoalId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setupToolbar()
        setupWorkoutTypeDropdown()
        setupListeners()
        loadUserGoals()
        observeViewModel()
    }

    private fun loadUserGoals() {
        val user = sessionManager.getUser()
        val token = sessionManager.getAuthToken()

        if (user != null && token != null) {
            goalViewModel.getUserGoals(token, user.id)
        }
    }

    private fun setupGoalDropdown() {
        val goalNames = userGoals.map { "${it.goalType} - ${it.targetValue}" }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, goalNames)
        binding.spinnerGoal.setAdapter(adapter)

        binding.spinnerGoal.setOnItemClickListener { _, _, position, _ ->
            selectedGoalId = userGoals[position].id
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        // Remove menu - only back button needed
        ToolbarUtils.tintToolbarIconsWhite(binding.toolbar)
    }

    private fun setupWorkoutTypeDropdown() {
        val workoutTypes = arrayOf("Running", "Weightlifting", "Cycling")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, workoutTypes)
        binding.spinnerWorkoutType.setAdapter(adapter)

        binding.spinnerWorkoutType.setOnItemClickListener { _, _, position, _ ->
            selectedWorkoutType = when (position) {
                0 -> "running"
                1 -> "weightlifting"
                2 -> "cycling"
                else -> "running"
            }
            updateFormVisibility()
        }
    }

    private fun updateFormVisibility() {
        binding.layoutRunning.visibility = if (selectedWorkoutType == "running") View.VISIBLE else View.GONE
        binding.layoutWeightlifting.visibility = if (selectedWorkoutType == "weightlifting") View.VISIBLE else View.GONE
        binding.layoutCycling.visibility = if (selectedWorkoutType == "cycling") View.VISIBLE else View.GONE
    }

    private fun setupListeners() {
        binding.btnSaveWorkout.setOnClickListener { saveWorkout() }
    }

    private fun saveWorkout() {
        val duration = binding.etDuration.text.toString().trim()
        val calories = binding.etCalories.text.toString().trim()

        if (duration.isEmpty() || calories.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val user = sessionManager.getUser()
        val token = sessionManager.getAuthToken()

        if (user == null || token == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val workout = when (selectedWorkoutType) {
            "running" -> {
                val distance = binding.etDistance.text.toString().trim()
                val pace = binding.etPace.text.toString().trim()

                if (distance.isEmpty() || pace.isEmpty()) {
                    Toast.makeText(this, "Please fill in all running details", Toast.LENGTH_SHORT).show()
                    return
                }

                WorkoutData(
                    userId = user.id,
                    goalId = selectedGoalId,
                    workoutType = selectedWorkoutType,
                    startTime = currentTime,
                    endTime = currentTime,
                    duration = duration.toInt(),
                    caloriesBurned = calories.toDouble(),
                    distance = distance.toDouble(),
                    averagePace = pace.toDouble()
                )
            }
            "weightlifting" -> {
                val exerciseName = binding.etExerciseName.text.toString().trim()
                val sets = binding.etSets.text.toString().trim()
                val reps = binding.etReps.text.toString().trim()
                val maxWeight = binding.etMaxWeight.text.toString().trim()

                if (exerciseName.isEmpty() || sets.isEmpty() || reps.isEmpty() || maxWeight.isEmpty()) {
                    Toast.makeText(this, "Please fill in all weightlifting details", Toast.LENGTH_SHORT).show()
                    return
                }

                WorkoutData(
                    userId = user.id,
                    goalId = selectedGoalId,
                    workoutType = selectedWorkoutType,
                    startTime = currentTime,
                    endTime = currentTime,
                    duration = duration.toInt(),
                    caloriesBurned = calories.toDouble(),
                    exerciseName = exerciseName,
                    totalSets = sets.toInt(),
                    totalReps = reps.toInt(),
                    maxWeight = maxWeight.toDouble()
                )
            }
            "cycling" -> {
                val distance = binding.etCyclingDistance.text.toString().trim()
                val speed = binding.etSpeed.text.toString().trim()

                if (distance.isEmpty() || speed.isEmpty()) {
                    Toast.makeText(this, "Please fill in all cycling details", Toast.LENGTH_SHORT).show()
                    return
                }

                WorkoutData(
                    userId = user.id,
                    goalId = selectedGoalId,
                    workoutType = selectedWorkoutType,
                    startTime = currentTime,
                    endTime = currentTime,
                    duration = duration.toInt(),
                    caloriesBurned = calories.toDouble(),
                    distance = distance.toDouble(),
                    averageSpeed = speed.toDouble()
                )
            }
            else -> return
        }

        workoutViewModel.createWorkout(token, workout)
    }

    private fun observeViewModel() {
        // Observe goals loading
        goalViewModel.goals.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    userGoals = result.data?.filter { !it.achieved } ?: emptyList()
                    setupGoalDropdown()
                }
                is Resource.Error -> {
                    Toast.makeText(this, "Failed to load goals", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        // Observe workout creation
        workoutViewModel.createWorkoutResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSaveWorkout.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val message = ErrorMessageHelper.getSuccessMessage("create_workout")
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSaveWorkout.isEnabled = true
                    val userMessage = ErrorMessageHelper.getUserFriendlyMessage(result.message)
                    Toast.makeText(this, userMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
