package com.example.auththentication.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auththentication.data.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Splash ViewModel - Business logic for splash screen
 *
 * Why ViewModel?
 * - Survives configuration changes (like screen rotation)
 * - Separates UI logic from UI rendering
 * - Lifecycle-aware
 *
 * @HiltViewModel
 * - Tells Hilt this is a ViewModel
 * @Inject
 * - Hilt provides PreferencesManager automatically
 */
@HiltViewModel
class SplashViewModel @Inject constructor(private val preferencesManager: PreferencesManager) :
        ViewModel() {

    /**
     * StateFlow - Like LiveData but for Compose
     *
     * Why StateFlow?
     * - UI observes this and updates automatically when value changes
     * - Type-safe
     * - Works perfectly with Compose
     */
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    /**
     * Check login status after 3 seconds delay
     *
     * Why viewModelScope.launch?
     * - Runs in background thread (coroutine)
     * - Automatically cancelled when ViewModel is destroyed
     * - Won't leak memory
     */
    fun checkLoginStatus() {
        viewModelScope.launch {
            // Show splash for 3 seconds
            delay(3000)

            // Check if user is logged in
            val loggedIn = preferencesManager.isLoggedIn()
            _isLoggedIn.value = loggedIn
        }
    }
}
