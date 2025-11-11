package com.example.stanleygersomp00199276fitnesstracker.utils

import com.example.stanleygersomp00199276fitnesstracker.models.Statistics
import com.example.stanleygersomp00199276fitnesstracker.models.WorkoutData
import java.time.LocalDate
import kotlin.math.round

/**
 * Analytics engine that computes statistics from a list of workouts.
 */
class FitnessAnalytics {

    companion object {
        // Removed unused imports and redundant initializers to reduce warnings

        fun computeStatistics(workouts: List<WorkoutData>): Statistics {
            if (workouts.isEmpty()) return Statistics()

            val totalWorkouts = workouts.size
            var totalDuration = 0
            var totalCalories = 0.0
            var totalDistance = 0.0
            val byType = mutableMapOf<String, Int>()

            // Weekly trends example: key = yyyy-'W'ww
            val weekly = mutableMapOf<String, Double>()

            var prevDate: LocalDate? = null

            val datesWithWorkout = mutableSetOf<LocalDate>()

            for (w in workouts) {
                totalDuration += w.duration
                totalCalories += w.caloriesBurned
                val distance = w.distance ?: 0.0
                totalDistance += distance

                byType[w.workoutType] = byType.getOrDefault(w.workoutType, 0) + 1

                // compute weekly key
                try {
                    val dateStr = w.startTime
                    if (dateStr.length >= 10) {
                        val dt = LocalDate.parse(dateStr.substring(0, 10))
                        datesWithWorkout.add(dt)
                        val weekNo = dt.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                        val weekKey = "${dt.year}-W$weekNo"
                        weekly[weekKey] = weekly.getOrDefault(weekKey, 0.0) + distance
                    }
                } catch (ex: Exception) {
                    // Log parse issue to console for debugging
                    ex.printStackTrace()
                }
            }

            // compute streaks
            val sortedDates = datesWithWorkout.toList().sorted()
            var currentStreak = 0
            var longestStreak = 0
            for (d in sortedDates) {
                if (prevDate == null) {
                    currentStreak = 1
                    longestStreak = maxOf(longestStreak, currentStreak)
                } else {
                    val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(prevDate, d)
                    if (daysBetween == 1L) {
                        currentStreak += 1
                        longestStreak = maxOf(longestStreak, currentStreak)
                    } else {
                        currentStreak = 1
                    }
                }
                prevDate = d
            }

            // If last workout was not today and gap >1, reset current streak to 0
            val today = LocalDate.now()
            val last = sortedDates.lastOrNull()
            if (last == null) {
                currentStreak = 0
            } else if (java.time.temporal.ChronoUnit.DAYS.between(last, today) > 1) {
                currentStreak = 0
            }

            val avgDuration = if (totalWorkouts > 0) totalDuration.toDouble() / totalWorkouts else 0.0
            val avgCalories = if (totalWorkouts > 0) totalCalories / totalWorkouts else 0.0

            return Statistics(
                totalWorkouts = totalWorkouts,
                totalDurationMinutes = totalDuration,
                totalCalories = round(totalCalories * 100) / 100.0,
                totalDistanceKm = round(totalDistance * 100) / 100.0,
                averageDurationMinutes = round(avgDuration * 100) / 100.0,
                averageCalories = round(avgCalories * 100) / 100.0,
                workoutsByType = byType,
                weeklyTrends = weekly,
                currentStreakDays = currentStreak,
                longestStreakDays = longestStreak
            )
        }
    }
}
