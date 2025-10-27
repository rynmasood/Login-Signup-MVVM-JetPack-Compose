package com.example.auththentication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.auththentication.presentation.dashboard.DashboardScreen
import com.example.auththentication.presentation.login.LoginScreen
import com.example.auththentication.presentation.signup.SignUpScreen
import com.example.auththentication.presentation.splash.SplashScreen

/**
 * Navigation Graph - Defines all screens and transitions
 *
 * Why NavHost?
 * - Central place to define all navigation routes
 * - Handles back stack automatically
 * - Integrates with Hilt for ViewModel injection
 *
 * @param navController
 * - Controls navigation (like going back, navigating to screen)
 * @param startDestination
 * - Which screen to show first
 */
@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Screen.Splash.route) {
    NavHost(navController = navController, startDestination = startDestination) {
        // Splash Screen
        composable(route = Screen.Splash.route) {
            SplashScreen(
                    onNavigateToLogin = {
                        // Navigate to login and clear splash from back stack
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToDashboard = {
                        // Navigate to dashboard and clear splash from back stack
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
            )
        }

        // Login Screen
        composable(route = Screen.Login.route) {
            LoginScreen(
                    onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                    onNavigateToDashboard = {
                        // Navigate to dashboard and clear login from back stack
                        // User can't press back to return to login
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
            )
        }

        // SignUp Screen
        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                    onNavigateBack = {
                        navController.popBackStack() // Go back to login
                    },
                    onSignUpSuccess = {
                        navController.popBackStack() // Go back to login after successful signup
                    }
            )
        }

        // Dashboard Screen
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                    onLogout = {
                        // Navigate to login and clear entire back stack
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true } // Clear everything
                        }
                    }
            )
        }
    }
}
