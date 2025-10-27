package com.example.auththentication.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Splash Screen UI
 *
 * Why Composable?
 * - Declarative UI (describe WHAT you want, not HOW to build it)
 * - Automatically updates when state changes
 * - Less boilerplate than XML layouts
 */
@Composable
fun SplashScreen(
        onNavigateToLogin: () -> Unit,
        onNavigateToDashboard: () -> Unit,
        viewModel: SplashViewModel = hiltViewModel() // Hilt provides ViewModel
) {
    /**
     * Collect state from ViewModel
     *
     * Why collectAsState?
     * - Converts StateFlow to Compose State
     * - UI recomposes (updates) when value changes
     */
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    /**
     * LaunchedEffect - Runs side effects in Composable
     *
     * Why LaunchedEffect?
     * - Runs only once when screen appears (key1 = Unit)
     * - Perfect for triggering one-time actions like checking login status
     */
    LaunchedEffect(Unit) { viewModel.checkLoginStatus() }

    /** Navigate based on login status When isLoggedIn changes from null to true/false, navigate */
    LaunchedEffect(isLoggedIn) {
        when (isLoggedIn) {
            true -> onNavigateToDashboard()
            false -> onNavigateToLogin()
            null -> {
                /* Still loading */
            }
        }
    }

    /** UI Layout Box - Like FrameLayout, stacks children on top of each other */
    Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center // Center all children
    ) {
        Text(
                text = "Welcome",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
        )

        // Show loading indicator at bottom
        Box(
                modifier = Modifier.fillMaxSize().align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary) }
    }
}
