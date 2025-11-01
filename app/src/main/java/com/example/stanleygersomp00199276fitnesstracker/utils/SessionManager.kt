package com.example.stanleygersomp00199276fitnesstracker.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.stanleygersomp00199276fitnesstracker.models.User
import com.google.gson.Gson

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    init {
        // Auto-heal: if token and user exist but login flag is false (from older versions), set it.
        val hasLoggedInFlag = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val token = prefs.getString(KEY_TOKEN, null)
        val userJson = prefs.getString(KEY_USER, null)
        if (!hasLoggedInFlag && !token.isNullOrEmpty() && !userJson.isNullOrEmpty()) {
            prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "fitness_tracker_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER = "user_data"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveAuthToken(token: String) {
        // Persist token; do not toggle login flag here to avoid marking logged in without user
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        prefs.edit().putString(KEY_USER, userJson).apply()
        // Do not mark as logged in here; wait until token exists to avoid inconsistent state
        // Leaving previous behavior disabled intentionally for consistency
        // prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
    }

    fun saveSession(user: User, token: String) {
        // Atomically persist user and token and mark logged-in
        val userJson = gson.toJson(user)
        prefs.edit()
            .putString(KEY_USER, userJson)
            .putString(KEY_TOKEN, token)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    fun getUser(): User? {
        val userJson = prefs.getString(KEY_USER, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        val hasLoggedInFlag = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val token = getAuthToken()
        return hasLoggedInFlag && !token.isNullOrEmpty()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
