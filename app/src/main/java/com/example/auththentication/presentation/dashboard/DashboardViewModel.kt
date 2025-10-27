package com.example.auththentication.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.example.auththentication.data.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Dashboard ViewModel */
@HiltViewModel
class DashboardViewModel @Inject constructor(private val preferencesManager: PreferencesManager) :
        ViewModel() {

    // User's full name
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    init {
        // Load user name when ViewModel is created
        _userName.value = preferencesManager.getUserName()
    }

    /**
     * Logout user
     * - Clears SharedPreferences
     */
    fun logout() {
        preferencesManager.clearLoginData()
    }
}
