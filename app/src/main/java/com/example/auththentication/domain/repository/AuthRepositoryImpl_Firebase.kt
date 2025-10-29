package com.example.auththentication.domain.repository

import com.example.auththentication.data.remote.FirebaseAuthService
import com.example.auththentication.domain.model.User
import javax.inject.Inject

/**
 * Firebase Repository Implementation
 * 
 * COMPARE WITH OLD (Room) IMPLEMENTATION:
 * 
 * OLD: Used UserDao (Room Database)
 * NEW: Uses FirebaseAuthService
 * 
 * NOTICE: The interface (AuthRepository) stays the SAME!
 * This is why ViewModels don't need to change at all!
 */
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthService: FirebaseAuthService  // ← ONLY THIS CHANGED!
    // OLD: private val userDao: UserDao
) : AuthRepository {

    /**
     * Register a new user
     * 
     * CHANGED: Now calls Firebase instead of Room
     */
    override suspend fun registerUser(user: User): Result<User> {
        return try {
            // Check if email already exists
            if (firebaseAuthService.isEmailExists(user.email)) {
                return Result.failure(Exception("Email already registered"))
            }

            // Register with Firebase
            // OLD: userDao.insertUser(userEntity)
            // NEW: firebaseAuthService.registerUser()
            val result = firebaseAuthService.registerUser(
                email = user.email,
                password = user.password,
                fullName = user.fullName
            )

            if (result.isSuccess) {
                Result.success(user)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Login user
     * 
     * CHANGED: Now calls Firebase instead of Room
     */
    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            // Login with Firebase
            // OLD: val userEntity = userDao.getUserByEmailAndPassword(email, password)
            // NEW: firebaseAuthService.loginUser(email, password)
            val result = firebaseAuthService.loginUser(email, password)

            if (result.isSuccess) {
                val (userId, userName) = result.getOrNull() ?: return Result.failure(Exception("Login failed"))
                
                // Create User object
                val user = User(
                    email = email,
                    fullName = userName,
                    password = password  // Note: In real app, don't store password!
                )
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Check if email exists
     * 
     * CHANGED: Now calls Firebase instead of Room
     */
    override suspend fun isEmailExists(email: String): Boolean {
        return try {
            // OLD: userDao.isEmailExists(email)
            // NEW: firebaseAuthService.isEmailExists(email)
            firebaseAuthService.isEmailExists(email)
        } catch (e: Exception) {
            false
        }
    }
}
