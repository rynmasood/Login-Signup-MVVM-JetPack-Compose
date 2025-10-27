package com.example.auththentication.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

/** Login Screen UI */
@Composable
fun LoginScreen(
        onNavigateToSignUp: () -> Unit,
        onNavigateToDashboard: () -> Unit,
        viewModel: LoginViewModel = hiltViewModel()
) {
    // Collect states
    val loginState by viewModel.loginState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    /** Handle navigation when login succeeds */
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onNavigateToDashboard()
        }
    }

    /** Show error dialog */
    if (loginState is LoginState.Error) {
        AlertDialog(
                onDismissRequest = { viewModel.resetState() },
                title = { Text("Login Failed") },
                text = { Text((loginState as LoginState.Error).message) },
                confirmButton = { TextButton(onClick = { viewModel.resetState() }) { Text("OK") } }
        )
    }

    /** Main UI Layout Column - Arranges children vertically */
    Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email TextField
        OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                placeholder = { Text("example@email.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Password TextField
        OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                placeholder = { Text("Minimum 6 characters") },
                visualTransformation = PasswordVisualTransformation(), // Hide password
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        // Login Button
        Button(
                onClick = { viewModel.login() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = loginState !is LoginState.Loading // Disable during loading
        ) {
            if (loginState is LoginState.Loading) {
                CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Login", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigate to SignUp
        TextButton(onClick = onNavigateToSignUp) { Text("Don't have an account? Sign Up") }
    }
}
