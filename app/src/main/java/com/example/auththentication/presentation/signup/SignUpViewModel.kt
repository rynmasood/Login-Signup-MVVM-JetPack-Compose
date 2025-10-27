package com.example.auththentication.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auththentication.domain.model.User
import com.example.auththentication.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** SignUp ViewModel - Handles signup business logic */
@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository) :
        ViewModel() {

    // SignUp state
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    // Form fields
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    /** Update form fields */
    fun onFullNameChange(name: String) {
        _fullName.value = name
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChange(confirmPass: String) {
        _confirmPassword.value = confirmPass
    }

    /** Validate all inputs */
    private fun validateInputs(): String? {
        if (_fullName.value.isBlank()) {
            return "Please enter your full name"
        }

        if (!_email.value.contains("@")) {
            return "Please enter a valid email with @"
        }

        if (_password.value.length < 6) {
            return "Password must be at least 6 characters"
        }

        if (_password.value != _confirmPassword.value) {
            return "Passwords do not match"
        }

        return null // All validations passed
    }

    /**
     * Register user
     *
     * Flow:
     * 1. Validate all inputs
     * 2. Show loading state
     * 3. Create User object
     * 4. Call repository to save user
     * 5. Update state
     */
    fun signUp() {
        viewModelScope.launch {
            // Validate
            val validationError = validateInputs()
            if (validationError != null) {
                _signUpState.value = SignUpState.Error(validationError)
                return@launch
            }

            // Show loading
            _signUpState.value = SignUpState.Loading

            // Create user
            val user =
                    User(
                            email = _email.value,
                            fullName = _fullName.value,
                            password = _password.value
                    )

            // Register
            val result = authRepository.registerUser(user)

            result.fold(
                    onSuccess = { _signUpState.value = SignUpState.Success },
                    onFailure = { exception ->
                        _signUpState.value =
                                SignUpState.Error(exception.message ?: "Registration failed")
                    }
            )
        }
    }

    /** Reset state to idle */
    fun resetState() {
        _signUpState.value = SignUpState.Idle
    }
}
