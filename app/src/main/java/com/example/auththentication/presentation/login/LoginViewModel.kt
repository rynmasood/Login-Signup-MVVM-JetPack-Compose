package com.example.auththentication.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auththentication.data.preferences.PreferencesManager
import com.example.auththentication.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Login ViewModel - Handles login business logic
 *
 * Why ViewModel?
 * - Survives configuration changes
 * - Separates business logic from UI
 * - Easy to test
 */
@HiltViewModel
class LoginViewModel
@Inject
constructor(
        private val authRepository: AuthRepository,
        private val preferencesManager: PreferencesManager
) : ViewModel() {

    // Login state
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    // Form fields
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    /** Update email field */
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    /** Update password field */
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    /**
     * Validate email
     * - Must not be empty
     * - Must contain @
     */
    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && email.contains("@")
    }

    /**
     * Validate password
     * - Must be at least 6 characters
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    /**
     * Login user
     *
     * Flow:
     * 1. Validate inputs
     * 2. Show loading state
     * 3. Call repository to check credentials
     * 4. Save login status if successful
     * 5. Update state
     */
    fun login() {
        viewModelScope.launch {
            // Validate inputs
            if (!isEmailValid(_email.value)) {
                _loginState.value = LoginState.Error("Please enter a valid email with @")
                return@launch
            }

            if (!isPasswordValid(_password.value)) {
                _loginState.value = LoginState.Error("Password must be at least 6 characters")
                return@launch
            }

            // Show loading
            _loginState.value = LoginState.Loading

            // Attempt login
            val result = authRepository.loginUser(_email.value, _password.value)

            result.fold(
                    onSuccess = { user ->
                        // Save login status
                        preferencesManager.saveLoginStatus(
                                isLoggedIn = true,
                                userName = user.fullName,
                                userEmail = user.email
                        )
                        _loginState.value = LoginState.Success(user.fullName)
                    },
                    onFailure = { exception ->
                        _loginState.value = LoginState.Error(exception.message ?: "Login failed")
                    }
            )
        }
    }

    /** Reset state to idle Useful after showing error message */
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
