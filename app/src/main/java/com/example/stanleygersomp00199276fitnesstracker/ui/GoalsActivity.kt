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
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager
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

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_profile -> {
                    startActivity(android.content.Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.action_about -> {
                    startActivity(android.content.Intent(this, AboutActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        goalAdapter = GoalAdapter()
        binding.recyclerViewGoals.apply {
            layoutManager = LinearLayoutManager(this@GoalsActivity)
            adapter = goalAdapter
        }
    }

    private fun setupListeners() {
        binding.btnAddGoal.setOnClickListener {
            Toast.makeText(this, "Add Goal feature - Implement dialog or activity", Toast.LENGTH_SHORT).show()
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
    }
}
