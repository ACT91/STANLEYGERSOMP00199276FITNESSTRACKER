package com.example.stanleygersomp00199276fitnesstracker.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityAchievementsBinding
import com.example.stanleygersomp00199276fitnesstracker.repository.Resource
import com.example.stanleygersomp00199276fitnesstracker.utils.SessionManager
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils
import com.example.stanleygersomp00199276fitnesstracker.viewmodel.AchievementViewModel

class AchievementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAchievementsBinding
    private val viewModel: AchievementViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var achievementAdapter: com.example.stanleygersomp00199276fitnesstracker.ui.AchievementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.gunmetal_green)

        sessionManager = SessionManager(this)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        loadAchievements()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.toolbar.inflateMenu(R.menu.menu_main)
        ToolbarUtils.tintToolbarIconsWhite(binding.toolbar)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_about -> {
                    startActivity(android.content.Intent(this, AboutActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        achievementAdapter = com.example.stanleygersomp00199276fitnesstracker.ui.AchievementAdapter(emptyList())
        binding.recyclerViewAchievements.apply {
            layoutManager = LinearLayoutManager(this@AchievementsActivity)
            adapter = achievementAdapter
        }
    }

    private fun loadAchievements() {
        val user = sessionManager.getUser()
        val token = sessionManager.getAuthToken()

        if (user != null && token != null) {
            viewModel.getUserAchievements(token, user.id)
        }
    }

    private fun observeViewModel() {
        viewModel.achievements.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewAchievements.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val achievements = result.data ?: emptyList()
                    if (achievements.isEmpty()) {
                        binding.tvEmptyState.visibility = View.VISIBLE
                        binding.recyclerViewAchievements.visibility = View.GONE
                    } else {
                        binding.tvEmptyState.visibility = View.GONE
                        binding.recyclerViewAchievements.visibility = View.VISIBLE
                        achievementAdapter = com.example.stanleygersomp00199276fitnesstracker.ui.AchievementAdapter(achievements)
                        binding.recyclerViewAchievements.adapter = achievementAdapter
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.VISIBLE
                    Toast.makeText(this, "Failed to load achievements", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

