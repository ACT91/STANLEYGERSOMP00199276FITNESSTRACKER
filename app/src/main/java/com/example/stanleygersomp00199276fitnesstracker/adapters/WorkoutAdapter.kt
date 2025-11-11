package com.example.stanleygersomp00199276fitnesstracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.databinding.ItemWorkoutBinding
import com.example.stanleygersomp00199276fitnesstracker.models.WorkoutData

class WorkoutAdapter(
    private val onDeleteClick: (Int) -> Unit
) : ListAdapter<WorkoutData, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
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

    class WorkoutViewHolder(
        private val binding: ItemWorkoutBinding,
        private val onDeleteClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: WorkoutData) {
            // Set workout type with proper formatting
            binding.tvWorkoutType.text = workout.workoutType
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

            // Set workout date
            binding.tvWorkoutDate.text = workout.startTime

            // Set duration (just the number)
            binding.tvDuration.text = workout.duration.toString()

            // Set calories (just the number)
            binding.tvCalories.text = workout.caloriesBurned.toInt().toString()

            // Set workout type icon
            val iconRes = when (workout.workoutType.lowercase()) {
                "running" -> com.example.stanleygersomp00199276fitnesstracker.R.drawable.ic_fitness
                "cycling" -> com.example.stanleygersomp00199276fitnesstracker.R.drawable.ic_fitness
                "weightlifting" -> com.example.stanleygersomp00199276fitnesstracker.R.drawable.ic_fitness
                else -> com.example.stanleygersomp00199276fitnesstracker.R.drawable.ic_fitness
            }
            binding.ivWorkoutIcon.setImageResource(iconRes)

            // Display distance and additional details based on workout type
            when (workout.workoutType.lowercase()) {
                "running" -> {
                    binding.layoutDistance.visibility = android.view.View.VISIBLE
                    binding.tvDistance.text = workout.distance.toString()
                    binding.tvAdditionalDetails.visibility = android.view.View.VISIBLE
                    binding.tvAdditionalDetails.text = "Pace: ${workout.averagePace} min/km"
                }
                "cycling" -> {
                    binding.layoutDistance.visibility = android.view.View.VISIBLE
                    binding.tvDistance.text = workout.distance.toString()
                    binding.tvAdditionalDetails.visibility = android.view.View.VISIBLE
                    binding.tvAdditionalDetails.text = "Speed: ${workout.averageSpeed} km/h"
                }
                "weightlifting" -> {
                    binding.layoutDistance.visibility = android.view.View.GONE
                    binding.tvAdditionalDetails.visibility = android.view.View.VISIBLE
                    binding.tvAdditionalDetails.text = "${workout.exerciseName} - ${workout.totalSets} sets x ${workout.totalReps} reps @ ${workout.maxWeight} kg"
                }
                else -> {
                    binding.layoutDistance.visibility = android.view.View.GONE
                    binding.tvAdditionalDetails.visibility = android.view.View.GONE
                }
            }

            // Delete button click
            binding.ivDelete.setOnClickListener {
                onDeleteClick(workout.id)
            }
        }
    }

    class WorkoutDiffCallback : DiffUtil.ItemCallback<WorkoutData>() {
        override fun areItemsTheSame(oldItem: WorkoutData, newItem: WorkoutData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WorkoutData, newItem: WorkoutData): Boolean {
            return oldItem == newItem
        }
    }
}

