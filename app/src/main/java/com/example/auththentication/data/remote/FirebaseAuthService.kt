package com.example.auththentication.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase Authentication Service
 * 
 * Handles all Firebase Authentication operations
 * This replaces Room Database (UserDao + AppDatabase)
 */
@Singleton
class FirebaseAuthService @Inject constructor() {
    
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    /**
     * Register new user with Firebase Authentication
     * 
     * Replaces: userDao.insertUser()
     */
    suspend fun registerUser(email: String, password: String, fullName: String): Result<String> {
        return try {
            // Create user in Firebase Auth
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
            
            val userId = authResult.user?.uid ?: return Result.failure(Exception("User creation failed"))
            
            // Update display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build()
            authResult.user?.updateProfile(profileUpdates)?.await()
            
            // Save additional user data to Firestore
            val userData = hashMapOf(
                "email" to email,
                "fullName" to fullName,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("users")
                .document(userId)
                .set(userData)
                .await()
            
            Result.success(userId)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("Email already registered"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Login user with Firebase Authentication
     * 
     * Replaces: userDao.getUserByEmailAndPassword()
     */
    suspend fun loginUser(email: String, password: String): Result<Pair<String, String>> {
        return try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            
            val userId = authResult.user?.uid ?: return Result.failure(Exception("Login failed"))
            val userName = authResult.user?.displayName ?: ""
            
            Result.success(Pair(userId, userName))
        } catch (e: Exception) {
            Result.failure(Exception("Invalid email or password"))
        }
    }
    
    /**
     * Check if email exists
     * 
     * Replaces: userDao.isEmailExists()
     */
    suspend fun isEmailExists(email: String): Boolean {
        return try {
            val methods = firebaseAuth.fetchSignInMethodsForEmail(email).await()
            !methods.signInMethods.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get current logged-in user
     */
    fun getCurrentUser() = firebaseAuth.currentUser
    
    /**
     * Logout current user
     */
    fun logout() {
        firebaseAuth.signOut()
    }
}
