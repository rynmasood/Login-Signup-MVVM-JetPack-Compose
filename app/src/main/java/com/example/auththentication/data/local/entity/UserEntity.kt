package com.example.auththentication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity - Represents the User table in our local database
 *
 * Why Entity?
 * - It's the data model that Room uses to create database tables
 * - Each field becomes a column in the database
 * - @PrimaryKey makes email unique (user can't register with same email twice)
 */
@Entity(tableName = "users")
data class UserEntity(
        @PrimaryKey val email: String, // Primary key - unique identifier
        val fullName: String, // User's full name
        val password: String // User's password (in real app, should be hashed!)
)
