package com.example.stanleygersomp00199276fitnesstracker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityTrackWorkoutBinding
import com.example.stanleygersomp00199276fitnesstracker.utils.LocationManager
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class TrackWorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackWorkoutBinding
    private lateinit var locationManager: LocationManager
    private var locationJob: Job? = null

    private var isTracking = false
    private var totalDistance = 0f // in meters
    private var lastLocation: Location? = null
    private var startTime = 0L
    private var pauseOffset = 0L

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                // Precise location access granted
                initializeLocationTracking()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                // Approximate location access granted
                initializeLocationTracking()
            }
            else -> {
                Toast.makeText(
                    this,
                    "Location permission is required for tracking",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = LocationManager(this)

        setupToolbar()
        setupListeners()
        checkLocationPermissions()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            if (isTracking) {
                showStopTrackingDialog()
            } else {
                finish()
            }
        }
        ToolbarUtils.tintToolbarIconsWhite(binding.toolbar)
    }

    private fun setupListeners() {
        binding.btnStartStop.setOnClickListener {
            if (isTracking) {
                pauseTracking()
            } else {
                startTracking()
            }
        }

        binding.btnReset.setOnClickListener {
            resetTracking()
        }

        binding.fabSaveWorkout.setOnClickListener {
            saveWorkout()
        }
    }

    private fun checkLocationPermissions() {
        when {
            locationManager.hasLocationPermission() -> {
                initializeLocationTracking()
            }
            else -> {
                locationPermissionRequest.launch(LocationManager.REQUIRED_PERMISSIONS)
            }
        }
    }

    private fun initializeLocationTracking() {
        binding.btnStartStop.isEnabled = true
        updateLocationDisplay(null)
    }

    private fun startTracking() {
        if (!locationManager.hasLocationPermission()) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
            return
        }

        isTracking = true
        binding.btnStartStop.text = "Pause"
        binding.btnStartStop.setIconResource(R.drawable.ic_pause)
        binding.fabSaveWorkout.visibility = View.VISIBLE

        if (startTime == 0L) {
            startTime = SystemClock.elapsedRealtime()
        } else {
            startTime = SystemClock.elapsedRealtime() - pauseOffset
        }

        binding.chronometer.base = startTime
        binding.chronometer.start()

        locationJob = lifecycleScope.launch {
            locationManager.getLocationUpdates()
                .catch { e ->
                    Toast.makeText(
                        this@TrackWorkoutActivity,
                        "Error tracking location: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .collect { location ->
                    updateLocation(location)
                }
        }
    }

    private fun pauseTracking() {
        isTracking = false
        binding.btnStartStop.text = "Resume"
        binding.btnStartStop.setIconResource(R.drawable.ic_play)

        pauseOffset = SystemClock.elapsedRealtime() - binding.chronometer.base
        binding.chronometer.stop()

        locationJob?.cancel()
        locationJob = null
    }

    private fun resetTracking() {
        pauseTracking()

        totalDistance = 0f
        lastLocation = null
        startTime = 0L
        pauseOffset = 0L

        binding.chronometer.base = SystemClock.elapsedRealtime()
        binding.btnStartStop.text = "Start Tracking"
        binding.btnStartStop.setIconResource(R.drawable.ic_play)
        binding.fabSaveWorkout.visibility = View.GONE

        updateStats()
    }

    private fun updateLocation(location: Location) {
        lastLocation?.let { last ->
            val distance = locationManager.calculateDistance(
                last.latitude, last.longitude,
                location.latitude, location.longitude
            )

            // Only add distance if it's reasonable (less than 100m between updates)
            if (distance < 100) {
                totalDistance += distance
            }
        }

        lastLocation = location
        updateLocationDisplay(location)
        updateStats()
    }

    private fun updateLocationDisplay(location: Location?) {
        if (location != null) {
            binding.tvLatitude.text = "Lat: %.6f".format(location.latitude)
            binding.tvLongitude.text = "Lon: %.6f".format(location.longitude)
            binding.tvAccuracy.text = "Accuracy: ±${location.accuracy.roundToInt()}m"

            if (location.hasSpeed()) {
                val speedKmh = location.speed * 3.6 // m/s to km/h
                binding.tvCurrentSpeed.text = "%.1f km/h".format(speedKmh)
            }
        } else {
            binding.tvLatitude.text = "Lat: --"
            binding.tvLongitude.text = "Lon: --"
            binding.tvAccuracy.text = "Accuracy: --"
            binding.tvCurrentSpeed.text = "-- km/h"
        }
    }

    private fun updateStats() {
        val distanceKm = totalDistance / 1000.0
        binding.tvDistance.text = "%.2f km".format(distanceKm)

        val durationSeconds = if (isTracking) {
            (SystemClock.elapsedRealtime() - binding.chronometer.base) / 1000
        } else {
            pauseOffset / 1000
        }

        if (distanceKm > 0 && durationSeconds > 0) {
            val speed = locationManager.calculateSpeed(totalDistance, durationSeconds)
            binding.tvAvgSpeed.text = "Avg: %.1f km/h".format(speed)

            val paceMinPerKm = (durationSeconds / 60.0) / distanceKm
            binding.tvPace.text = "Pace: %.1f min/km".format(paceMinPerKm)

            // Estimate calories (rough estimate: 60 calories per km for running)
            val estimatedCalories = (distanceKm * 60).roundToInt()
            binding.tvCalories.text = "$estimatedCalories kcal"
        } else {
            binding.tvAvgSpeed.text = "Avg: -- km/h"
            binding.tvPace.text = "Pace: -- min/km"
            binding.tvCalories.text = "-- kcal"
        }
    }

    private fun saveWorkout() {
        if (totalDistance < 10) { // Less than 10 meters
            Toast.makeText(this, "Distance too short to save", Toast.LENGTH_SHORT).show()
            return
        }

        pauseTracking()

        // TODO: Integrate with your existing workout save functionality
        val distanceKm = totalDistance / 1000.0
        val durationMinutes = (pauseOffset / 1000 / 60).toInt()
        val estimatedCalories = (distanceKm * 60).roundToInt()

        Toast.makeText(
            this,
            "Workout saved!\nDistance: %.2f km\nDuration: %d min\nCalories: %d kcal".format(
                distanceKm, durationMinutes, estimatedCalories
            ),
            Toast.LENGTH_LONG
        ).show()

        finish()
    }

    private fun showStopTrackingDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Stop Tracking")
            .setMessage("Do you want to stop tracking and discard this workout?")
            .setPositiveButton("Stop") { _, _ ->
                locationJob?.cancel()
                finish()
            }
            .setNegativeButton("Continue", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationJob?.cancel()
    }
}

