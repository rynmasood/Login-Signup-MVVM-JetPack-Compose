package com.example.auththentication.domain.repository

import com.example.auththentication.domain.model.User

/**
 * Repository Interface - Defines what operations are available
 *
 * Why interface?
 * - Follows Dependency Inversion Principle (depend on abstractions, not concrete implementations)
 * - Makes testing easier (can create mock implementations)
 * - UI layer doesn't need to know HOW data is stored, just WHAT operations are available
 */
interface AuthRepository {

    /**
     * Register a new user
     * @return Result wrapper - Success with User or Failure with error message
     */
    suspend fun registerUser(user: User): Result<User>

    /**
     * Login user with credentials
     * @return Result wrapper - Success with User or Failure with error message
     */
    suspend fun loginUser(email: String, password: String): Result<User>

    /** Check if email already exists */
    suspend fun isEmailExists(email: String): Boolean
}
