package com.example.auththentication.presentation.navigation

/**
 * Sealed class for Navigation Routes
 *
 * Why sealed class?
 * - Type-safe navigation - compiler ensures we use valid routes
 * - Can't create invalid routes
 * - Easy to add new screens
 *
 * Why object vs data class?
 * - object: For screens without parameters (Splash, Login, etc.)
 * - data class: For screens that need parameters (not used here, but good to know)
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Dashboard : Screen("dashboard")
}
