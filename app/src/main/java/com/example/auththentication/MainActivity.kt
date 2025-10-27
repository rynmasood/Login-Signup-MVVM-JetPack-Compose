package com.example.auththentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.auththentication.presentation.navigation.NavGraph
import com.example.auththentication.ui.theme.AuththenticationTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity - Entry point of the app
 * 
 * @AndroidEntryPoint - Enables Hilt dependency injection
 * Without this annotation, Hilt won't work in this Activity
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuththenticationTheme {
                /**
                 * Surface - Container that uses MaterialTheme colors
                 */
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /**
                     * rememberNavController - Creates NavController
                     * NavController manages navigation between screens
                     */
                    val navController = rememberNavController()
                    
                    /**
                     * NavGraph - Sets up all navigation routes
                     */
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
