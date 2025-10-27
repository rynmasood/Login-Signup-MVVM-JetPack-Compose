package com.example.auththentication.presentation.signup

/** SignUp State - Represents all possible states of signup screen */
sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState() // SignUp successful, navigate back to login
    data class Error(val message: String) : SignUpState()
}
