package com.example.auththentication.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages SharedPreferences - stores simple key-value data
 *
 * Why SharedPreferences?
 * - Perfect for storing small amounts of data like login status
 * - Persists across app restarts
 * - Faster than database for simple values
 *
 * @Singleton
 * - Only one instance exists in the entire app
 * @Inject
 * - Hilt will provide the Context automatically
 */
@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "auth_preferences"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
    }

    /** Save login status and user details after successful login */
    fun saveLoginStatus(isLoggedIn: Boolean, userName: String = "", userEmail: String = "") {
        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            apply() // Asynchronous save
        }
    }

    /** Check if user is logged in */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /** Get logged-in user's name */
    fun getUserName(): String {
        return sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
    }

    /** Get logged-in user's email */
    fun getUserEmail(): String {
        return sharedPreferences.getString(KEY_USER_EMAIL, "") ?: ""
    }

    /** Clear all data on logout */
    fun clearLoginData() {
        sharedPreferences.edit().clear().apply()
    }
}
