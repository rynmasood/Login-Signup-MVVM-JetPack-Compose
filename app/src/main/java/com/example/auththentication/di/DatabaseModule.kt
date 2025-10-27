package com.example.auththentication.di

import android.content.Context
import androidx.room.Room
import com.example.auththentication.data.local.dao.UserDao
import com.example.auththentication.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database Module - Provides database-related dependencies
 *
 * Why Module?
 * - Tells Hilt HOW to create instances of classes
 * - @InstallIn(SingletonComponent::class) - These dependencies live as long as the app
 * - @Provides - Marks methods that create dependencies
 * - @Singleton - Only one instance is created and shared
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the Room Database instance
     *
     * Why @Singleton?
     * - Database is expensive to create
     * - Should only have one instance
     * - Shared across entire app
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "auth_database" // Database name
                )
                .fallbackToDestructiveMigration() // If schema changes, recreate DB (for
                // development)
                .build()
    }

    /**
     * Provides UserDao from the database
     *
     * Why separate method?
     * - Hilt can inject UserDao directly where needed
     * - Don't need to inject entire database just to get DAO
     */
    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}
