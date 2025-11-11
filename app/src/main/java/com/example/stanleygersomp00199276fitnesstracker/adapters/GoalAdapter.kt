package com.example.stanleygersomp00199276fitnesstracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.databinding.ItemGoalBinding
import com.example.stanleygersomp00199276fitnesstracker.models.FitnessGoal

class GoalAdapter(
    private val onEditClick: (FitnessGoal) -> Unit = {},
    private val onDeleteClick: (FitnessGoal) -> Unit = {}
) : ListAdapter<FitnessGoal, GoalAdapter.GoalViewHolder>(GoalDiffCallback()) {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GoalViewHolder(binding, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position))

        // Apply animation
        if (position > lastPosition) {
            holder.itemView.alpha = 0f
            holder.itemView.scaleX = 0.8f
            holder.itemView.scaleY = 0.8f
            holder.itemView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(250)
                .start()
            lastPosition = position
        }
    }

    class GoalViewHolder(
        private val binding: ItemGoalBinding,
        private val onEditClick: (FitnessGoal) -> Unit,
        private val onDeleteClick: (FitnessGoal) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(goal: FitnessGoal) {
            // Set goal type with proper formatting
            binding.tvGoalType.text = goal.goalType.replace("_", " ")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

            // Set target value
            binding.tvTargetValue.text = "${goal.targetValue}"

            // Set deadline
            binding.tvDeadline.text = "Deadline: ${goal.deadline}"

            // Show/hide achievement icon
            if (goal.achieved) {
                binding.ivAchieved.visibility = android.view.View.VISIBLE
            } else {
                binding.ivAchieved.visibility = android.view.View.GONE
            }

            // Calculate and show progress if goal has current value
            if (goal.currentValue > 0 && goal.targetValue > 0) {
                binding.layoutProgress.visibility = android.view.View.VISIBLE
                val progress = ((goal.currentValue / goal.targetValue) * 100).toInt()
                binding.progressBar.progress = progress
                binding.tvProgress.text = "Progress: $progress%"
            } else {
                binding.layoutProgress.visibility = android.view.View.GONE
            }

            // Set click listeners for edit and delete buttons
            binding.btnEditGoal.setOnClickListener {
                onEditClick(goal)
            }

            binding.btnDeleteGoal.setOnClickListener {
                onDeleteClick(goal)
            }
        }
    }

    class GoalDiffCallback : DiffUtil.ItemCallback<FitnessGoal>() {
        override fun areItemsTheSame(oldItem: FitnessGoal, newItem: FitnessGoal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FitnessGoal, newItem: FitnessGoal): Boolean {
            return oldItem == newItem
        }
    }
}

