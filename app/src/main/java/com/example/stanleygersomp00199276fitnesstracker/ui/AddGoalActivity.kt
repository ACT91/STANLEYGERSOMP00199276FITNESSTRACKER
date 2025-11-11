package com.example.stanleygersomp00199276fitnesstracker.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityAddGoalBinding
import com.example.stanleygersomp00199276fitnesstracker.models.FitnessGoal
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.GoalViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGoalBinding
    private val viewModel: GoalViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private var selectedGoalType = "workout_count"
    private var selectedDeadline = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setupToolbar()
        setupGoalTypeDropdown()
        setupListeners()
        observeViewModel()
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

        binding.btnSaveGoal.setOnClickListener {
            saveGoal()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
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

    private fun saveGoal() {
        val targetValue = binding.etTargetValue.text.toString().trim()

        if (targetValue.isEmpty()) {
            Toast.makeText(this, "Please enter target value", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDeadline.isEmpty()) {
            Toast.makeText(this, "Please select a deadline", Toast.LENGTH_SHORT).show()
            return
        }

        val user = sessionManager.getUser()
        val token = sessionManager.getAuthToken()

        if (user == null || token == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        val goal = FitnessGoal(
            userId = user.id,
            goalType = selectedGoalType,
            targetValue = targetValue.toDouble(),
            currentValue = 0.0,
            deadline = selectedDeadline,
            achieved = false
        )

        viewModel.createGoal(token, goal)
    }

    private fun observeViewModel() {
        viewModel.createGoalResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSaveGoal.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Goal created successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSaveGoal.isEnabled = true
                    Toast.makeText(this, "Failed to create goal: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

