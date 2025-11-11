package com.example.stanleygersomp00199276fitnesstracker.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityEditGoalBinding
import com.example.stanleygersomp00199276fitnesstracker.models.FitnessGoal
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.GoalViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditGoalBinding
    private val viewModel: GoalViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private var selectedGoalType = "workout_count"
    private var selectedDeadline = ""
    private var goalId = 0
    private var currentValue = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Get goal data from intent
        goalId = intent.getIntExtra("GOAL_ID", 0)
        selectedGoalType = intent.getStringExtra("GOAL_TYPE") ?: "workout_count"
        val targetValue = intent.getDoubleExtra("TARGET_VALUE", 0.0)
        currentValue = intent.getDoubleExtra("CURRENT_VALUE", 0.0)
        selectedDeadline = intent.getStringExtra("DEADLINE") ?: ""

        setupToolbar()
        setupGoalTypeDropdown()
        setupListeners()
        observeViewModel()

        // Pre-fill form
        binding.etTargetValue.setText(targetValue.toString())
        binding.tvSelectedDeadline.text = selectedDeadline

        // Set goal type in dropdown
        val goalTypeIndex = when (selectedGoalType) {
            "workout_count" -> 0
            "distance" -> 1
            "calories" -> 2
            "duration" -> 3
            else -> 0
        }
        val goalTypes = arrayOf("Workout Count", "Distance (km)", "Calories", "Duration (min)")
        binding.spinnerGoalType.setText(goalTypes[goalTypeIndex], false)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        ToolbarUtils.tintToolbarIconsWhite(binding.toolbar)
    }

    private fun setupGoalTypeDropdown() {
        val goalTypes = arrayOf("Workout Count", "Distance (km)", "Calories", "Duration (min)")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, goalTypes)
        binding.spinnerGoalType.setAdapter(adapter)

        binding.spinnerGoalType.setOnItemClickListener { _, _, position, _ ->
            selectedGoalType = when (position) {
                0 -> "workout_count"
                1 -> "distance"
                2 -> "calories"
                3 -> "duration"
                else -> "workout_count"
            }
        }
    }

    private fun setupListeners() {
        binding.btnSelectDeadline.setOnClickListener {
            showDatePicker()
        }

        binding.btnUpdateGoal.setOnClickListener {
            updateGoal()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        // Parse current deadline
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            calendar.time = dateFormat.parse(selectedDeadline) ?: Date()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                selectedDeadline = dateFormat.format(calendar.time)
                binding.tvSelectedDeadline.text = selectedDeadline
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateGoal() {
        val targetValue = binding.etTargetValue.text.toString().trim()

        if (targetValue.isEmpty()) {
            Toast.makeText(this, "Please enter target value", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDeadline.isEmpty()) {
            Toast.makeText(this, "Please select a deadline", Toast.LENGTH_SHORT).show()
            return
        }

        val token = sessionManager.getAuthToken()

        if (token == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        val user = sessionManager.getUser()
        val goal = FitnessGoal(
            id = goalId,
            userId = user?.id ?: 0,
            goalType = selectedGoalType,
            targetValue = targetValue.toDouble(),
            currentValue = currentValue,
            deadline = selectedDeadline,
            achieved = false
        )

        viewModel.updateGoal(token, goalId, goal)
    }

    private fun observeViewModel() {
        viewModel.updateGoalResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnUpdateGoal.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Goal updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnUpdateGoal.isEnabled = true
                    Toast.makeText(this, "Failed to update goal: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

