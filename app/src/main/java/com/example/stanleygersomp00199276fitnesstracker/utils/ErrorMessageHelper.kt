package com.example.stanleygersomp00199276fitnesstracker.utils

object ErrorMessageHelper {

    /**
     * Converts API error messages to user-friendly messages
     */
    fun getUserFriendlyMessage(apiMessage: String?): String {
        return when {
            apiMessage == null -> "An error occurred. Please try again."

            // Authentication errors
            apiMessage.contains("Email already registered", ignoreCase = true) ->
                "This email is already in use. Please try logging in instead."

            apiMessage.contains("Invalid email or password", ignoreCase = true) ->
                "Incorrect email or password. Please check your credentials."

            apiMessage.contains("User not found", ignoreCase = true) ->
                "No account found with this email."

            apiMessage.contains("Invalid token", ignoreCase = true) ||
            apiMessage.contains("Unauthorized", ignoreCase = true) ->
                "Your session has expired. Please login again."

            // Validation errors
            apiMessage.contains("Missing required field", ignoreCase = true) ->
                "Please fill in all required fields."

            apiMessage.contains("Invalid email format", ignoreCase = true) ->
                "Please enter a valid email address."

            apiMessage.contains("Password must be at least", ignoreCase = true) ->
                "Password must be at least 6 characters long."

            // Network errors
            apiMessage.contains("Network error", ignoreCase = true) ->
                "No internet connection. Please check your network."

            apiMessage.contains("Timeout", ignoreCase = true) ->
                "Connection timed out. Please check your network and try again."

            apiMessage.contains("failed to connect", ignoreCase = true) ->
                "Cannot connect to server. Please check your internet connection."

            // Database/Server errors
            apiMessage.contains("Database", ignoreCase = true) ||
            apiMessage.contains("500", ignoreCase = true) ->
                "Server error. Please try again later."

            apiMessage.contains("404", ignoreCase = true) ->
                "Service not found. Please contact support."

            // Workout errors
            apiMessage.contains("Workout not found", ignoreCase = true) ->
                "This workout no longer exists."

            apiMessage.contains("Failed to create workout", ignoreCase = true) ->
                "Could not save workout. Please try again."

            apiMessage.contains("Failed to delete", ignoreCase = true) ->
                "Could not delete workout. Please try again."

            // Goal errors
            apiMessage.contains("Goal not found", ignoreCase = true) ->
                "This goal no longer exists."

            apiMessage.contains("Failed to create goal", ignoreCase = true) ->
                "Could not save goal. Please try again."

            // Default: return original message if it's user-friendly enough
            apiMessage.length < 100 && !apiMessage.contains("error", ignoreCase = true) ->
                apiMessage

            // Last resort
            else -> "Something went wrong. Please try again."
        }
    }

    /**
     * Gets user-friendly message based on HTTP status code
     */
    fun getMessageForStatusCode(statusCode: Int): String {
        return when (statusCode) {
            400 -> "Invalid request. Please check your input."
            401 -> "Please login to continue."
            403 -> "You don't have permission to perform this action."
            404 -> "Resource not found."
            409 -> "This action cannot be completed due to a conflict."
            500, 502, 503 -> "Server error. Please try again later."
            else -> "An error occurred. Please try again."
        }
    }

    /**
     * Formats success messages
     */
    fun getSuccessMessage(action: String): String {
        return when (action.lowercase()) {
            "register" -> "Account created successfully! Please login."
            "login" -> "Welcome back!"
            "logout" -> "You have been logged out."
            "create_workout" -> "Workout saved successfully!"
            "delete_workout" -> "Workout deleted."
            "create_goal" -> "Goal created successfully!"
            "update_goal" -> "Goal updated!"
            "update_profile" -> "Profile updated successfully!"
            "change_password" -> "Password changed successfully!"
            else -> "Operation completed successfully!"
        }
    }
}

