package com.example.auththentication.presentation.login

/**
 * Login State - Represents all possible states of login screen
 *
 * Why sealed class?
 * - Type-safe - compiler ensures we handle all states
 * - Each state can carry different data
 * - Perfect for representing UI states
 */
sealed class LoginState {
    object Idle : LoginState() // Initial state
    object Loading : LoginState() // Login in progress
    data class Success(val userName: String) : LoginState() // Login successful
    data class Error(val message: String) : LoginState() // Login failed
}
