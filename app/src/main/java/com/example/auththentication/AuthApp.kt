package com.example.auththentication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application Class - Required for Hilt
 *
 * @HiltAndroidApp
 * - Triggers Hilt's code generation This annotation must be on the Application class It sets up the
 * dependency injection container for the entire app
 *
 * Why Application class?
 * - Lives for the entire lifetime of the app
 * - Perfect place to initialize app-wide components
 * - Hilt uses this to set up all dependencies
 */
@HiltAndroidApp class AuthApp : Application()
