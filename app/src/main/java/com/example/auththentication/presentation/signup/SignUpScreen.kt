package com.example.auththentication.presentation.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

/** SignUp Screen UI */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
        onNavigateBack: () -> Unit,
        onSignUpSuccess: () -> Unit,
        viewModel: SignUpViewModel = hiltViewModel()
) {
    // Collect states
    val signUpState by viewModel.signUpState.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    /** Navigate back to login on success */
    LaunchedEffect(signUpState) {
        if (signUpState is SignUpState.Success) {
            onSignUpSuccess()
        }
    }

    /** Show error dialog */
    if (signUpState is SignUpState.Error) {
        AlertDialog(
                onDismissRequest = { viewModel.resetState() },
                title = { Text("Sign Up Failed") },
                text = { Text((signUpState as SignUpState.Error).message) },
                confirmButton = { TextButton(onClick = { viewModel.resetState() }) { Text("OK") } }
        )
    }

    /**
     * Scaffold - Provides basic Material Design layout structure Includes TopAppBar, content area,
     * etc.
     */
    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Sign Up") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                        }
                )
            }
    ) { paddingValues ->
        /**
         * verticalScroll - Makes content scrollable Useful when keyboard appears and covers fields
         */
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(paddingValues)
                                .padding(24.dp)
                                .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            Text(
                    text = "Create Account",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
            )

            // Full Name Field
            OutlinedTextField(
                    value = fullName,
                    onValueChange = { viewModel.onFullNameChange(it) },
                    label = { Text("Full Name") },
                    placeholder = { Text("John Doe") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Email Field
            OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    placeholder = { Text("example@email.com") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Password Field
            OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Password") },
                    placeholder = { Text("Minimum 6 characters") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Confirm Password Field
            OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { viewModel.onConfirmPasswordChange(it) },
                    label = { Text("Confirm Password") },
                    placeholder = { Text("Re-enter password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            // SignUp Button
            Button(
                    onClick = { viewModel.signUp() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = signUpState !is SignUpState.Loading
            ) {
                if (signUpState is SignUpState.Loading) {
                    CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Sign Up", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigate back to Login
            TextButton(onClick = onNavigateBack) { Text("Already have an account? Login") }
        }
    }
}
