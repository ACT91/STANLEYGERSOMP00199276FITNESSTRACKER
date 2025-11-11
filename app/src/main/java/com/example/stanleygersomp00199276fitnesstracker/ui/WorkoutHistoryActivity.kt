package com.example.stanleygersomp00199276fitnesstracker.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.adapters.WorkoutAdapter
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityWorkoutHistoryBinding
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import com.example.stanleygersomp00199276fitnesstracker.utils.ErrorMessageHelper
import com.example.stanleygersomp00199276fitnesstracker.utils.RecyclerViewAnimator
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.WorkoutViewModel

class WorkoutHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutHistoryBinding
    private val viewModel: WorkoutViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        sessionManager = SessionManager(this)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        loadWorkouts()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        // Tint navigation/menu icons white if any
        ToolbarUtils.tintToolbarIconsWhite(binding.toolbar)
    }

    private fun setupRecyclerView() {
        workoutAdapter = WorkoutAdapter { workoutId ->
            showDeleteDialog(workoutId)
        }
        binding.recyclerViewWorkouts.apply {
            layoutManager = LinearLayoutManager(this@WorkoutHistoryActivity)
            adapter = workoutAdapter
            // Apply animations
            RecyclerViewAnimator.applySlideInAnimation(this)
        }
    }

    private fun loadWorkouts() {
        val user = sessionManager.getUser()
        val token = sessionManager.getAuthToken()

        if (user != null && token != null) {
            viewModel.getUserWorkouts(token, user.id)
        }
    }

    private fun observeViewModel() {
        viewModel.workouts.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewWorkouts.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val workouts = result.data ?: emptyList()
                    if (workouts.isEmpty()) {
                        binding.tvEmptyState.visibility = View.VISIBLE
                        binding.recyclerViewWorkouts.visibility = View.GONE
                    } else {
                        binding.tvEmptyState.visibility = View.GONE
                        binding.recyclerViewWorkouts.visibility = View.VISIBLE
                        workoutAdapter.submitList(workouts)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.VISIBLE
                    val userMessage = ErrorMessageHelper.getUserFriendlyMessage(result.message)
                    Toast.makeText(this, userMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.deleteWorkoutResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val message = ErrorMessageHelper.getSuccessMessage("delete_workout")
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    loadWorkouts() // Reload workouts
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    val userMessage = ErrorMessageHelper.getUserFriendlyMessage(result.message)
                    Toast.makeText(this, userMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showDeleteDialog(workoutId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Workout")
            .setMessage("Are you sure you want to delete this workout?")
            .setPositiveButton("Delete") { _, _ ->
                val token = sessionManager.getAuthToken()
                if (token != null) {
                    viewModel.deleteWorkout(token, workoutId)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
