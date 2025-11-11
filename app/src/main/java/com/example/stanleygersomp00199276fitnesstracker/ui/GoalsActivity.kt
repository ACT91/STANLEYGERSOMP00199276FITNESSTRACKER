package com.example.stanleygersomp00199276fitnesstracker.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.adapters.GoalAdapter
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityGoalsBinding
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import com.example.stanleygersomp00199276fitnesstracker.utils.ErrorMessageHelper
import com.example.stanleygersomp00199276fitnesstracker.utils.RecyclerViewAnimator
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.GoalViewModel

class GoalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalsBinding
    private val viewModel: GoalViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var goalAdapter: GoalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.gunmetal_green)

        sessionManager = SessionManager(this)

        setupToolbar()
        setupRecyclerView()
        setupListeners()
        observeViewModel()
        loadGoals()
    }

    override fun onResume() {
        super.onResume()
        loadGoals() // Reload goals when returning from AddGoalActivity
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        // Remove menu - only back button needed
        ToolbarUtils.tintToolbarIconsWhite(binding.toolbar)
    }

    private fun setupRecyclerView() {
        goalAdapter = GoalAdapter(
            onEditClick = { goal ->
                // Launch EditGoalActivity with goal data
                val intent = android.content.Intent(this, EditGoalActivity::class.java).apply {
                    putExtra("GOAL_ID", goal.id)
                    putExtra("GOAL_TYPE", goal.goalType)
                    putExtra("TARGET_VALUE", goal.targetValue)
                    putExtra("CURRENT_VALUE", goal.currentValue)
                    putExtra("DEADLINE", goal.deadline)
                }
                startActivity(intent)
            },
            onDeleteClick = { goal ->
                deleteGoal(goal.id)
            }
        )
        binding.recyclerViewGoals.apply {
            layoutManager = LinearLayoutManager(this@GoalsActivity)
            adapter = goalAdapter
            // Apply animations
            RecyclerViewAnimator.applySlideInAnimation(this)
        }
    }

    private fun deleteGoal(goalId: Int) {
        val token = sessionManager.getAuthToken()
        if (token != null) {
            viewModel.deleteGoal(token, goalId)
        }
    }

    private fun setupListeners() {
        binding.btnAddGoal.setOnClickListener {
            startActivity(android.content.Intent(this, AddGoalActivity::class.java))
        }
    }

    private fun loadGoals() {
        val user = sessionManager.getUser()
        val token = sessionManager.getAuthToken()

        if (user != null && token != null) {
            viewModel.getUserGoals(token, user.id)
        }
    }

    private fun observeViewModel() {
        viewModel.goals.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewGoals.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val goals = result.data ?: emptyList()
                    if (goals.isEmpty()) {
                        binding.tvEmptyState.visibility = View.VISIBLE
                        binding.recyclerViewGoals.visibility = View.GONE
                    } else {
                        binding.tvEmptyState.visibility = View.GONE
                        binding.recyclerViewGoals.visibility = View.VISIBLE
                        goalAdapter.submitList(goals)
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

        viewModel.deleteGoalResult.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(this, "Goal deleted successfully", Toast.LENGTH_SHORT).show()
                    loadGoals() // Reload goals
                }
                is Resource.Error -> {
                    Toast.makeText(this, "Failed to delete goal", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}
