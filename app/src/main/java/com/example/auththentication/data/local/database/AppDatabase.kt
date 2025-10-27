package com.example.auththentication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.auththentication.data.local.dao.UserDao
import com.example.auththentication.data.local.entity.UserEntity

/**
 * Room Database - Main database configuration
 *
 * Why Database class?
 * - It's the main access point to our persisted data
 * - Room uses this to create the database
 * - It connects all DAOs and Entities together
 *
 * @Database annotation parameters:
 * - entities: List of all tables in the database
 * - version: Database version (increment when schema changes)
 * - exportSchema: Whether to export schema to a folder (useful for migrations)
 */
@Database(entities = [UserEntity::class], version = 1, exportSchema = true)
abstract class  AppDatabase : RoomDatabase() {

    /** Provides access to UserDao Room generates the implementation */
    abstract fun userDao(): UserDao
}
