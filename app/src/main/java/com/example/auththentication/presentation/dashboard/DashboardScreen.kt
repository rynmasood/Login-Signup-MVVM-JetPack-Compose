package com.example.auththentication.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

/** Dashboard Screen - Shows after successful login */
@Composable
fun DashboardScreen(onLogout: () -> Unit, viewModel: DashboardViewModel = hiltViewModel()) {
    // Get user name
    val userName by viewModel.userName.collectAsState()

    /** Show logout confirmation dialog */
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout") },
                text = { Text("Are you sure you want to logout?") },
                confirmButton = {
                    TextButton(
                            onClick = {
                                viewModel.logout()
                                showLogoutDialog = false
                                onLogout()
                            }
                    ) { Text("Yes") }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) { Text("No") }
                }
        )
    }

    /** Main UI */
    Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        // Greeting
        Text(text = "Welcome,", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)

        Spacer(modifier = Modifier.height(8.dp))

        // User's name
        Text(
                text = userName,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Logout Button
        Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("Logout", fontSize = 16.sp) }
    }
}
