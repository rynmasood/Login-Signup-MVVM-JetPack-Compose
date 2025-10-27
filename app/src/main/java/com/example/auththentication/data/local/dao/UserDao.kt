package com.example.auththentication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.auththentication.data.local.entity.UserEntity

/**
 * DAO (Data Access Object) - Defines database operations
 *
 * Why DAO?
 * - It provides an abstraction layer between the database and the rest of the app
 * - Room generates the implementation automatically
 * - All database operations happen through this interface
 */
@Dao
interface UserDao {

    /**
     * Insert a new user into the database OnConflictStrategy.REPLACE - If email exists, replace the
     * existing user
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertUser(user: UserEntity)

    /**
     * Get user by email and password (for login validation) Returns null if no matching user found
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?

    /** Check if email already exists (for signup validation) */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean
}
