package com.example.auththentication.data.repository

import com.example.auththentication.data.local.dao.UserDao
import com.example.auththentication.data.local.entity.UserEntity
import com.example.auththentication.domain.model.User
import com.example.auththentication.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Repository Implementation - Actual implementation of AuthRepository
 *
 * Why Repository Pattern?
 * - Single source of truth for data operations
 * - Abstracts data sources (could switch from Room to API without changing UI)
 * - Handles data transformation (Entity <-> Domain Model)
 *
 * @Inject
 * - Hilt will provide UserDao automatically
 */
class AuthRepositoryImpl @Inject constructor(private val userDao: UserDao) : AuthRepository {

    /**
     * Register a new user
     * - Checks if email already exists
     * - Converts domain User to UserEntity
     * - Saves to database
     */
    override suspend fun registerUser(user: User): Result<User> {
        return try {
            // Check if email already exists
            if (userDao.isEmailExists(user.email)) {
                return Result.failure(Exception("Email already registered"))
            }

            // Convert domain model to entity
            val userEntity =
                    UserEntity(
                            email = user.email,
                            fullName = user.fullName,
                            password = user.password
                    )

            // Save to database
            userDao.insertUser(userEntity)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Login user
     * - Queries database for matching credentials
     * - Converts UserEntity back to domain User
     */
    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val userEntity = userDao.getUserByEmailAndPassword(email, password)

            if (userEntity != null) {
                // Convert entity to domain model
                val user =
                        User(
                                email = userEntity.email,
                                fullName = userEntity.fullName,
                                password = userEntity.password
                        )
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Check if email exists in database */
    override suspend fun isEmailExists(email: String): Boolean {
        return try {
            userDao.isEmailExists(email)
        } catch (e: Exception) {
            false
        }
    }
}
