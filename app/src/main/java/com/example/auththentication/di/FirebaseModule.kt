package com.example.auththentication.di

import com.example.auththentication.data.remote.FirebaseAuthService
import com.example.auththentication.domain.repository.AuthRepository
import com.example.auththentication.domain.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Firebase Module - Provides Firebase dependencies
 * 
 * REPLACES: DatabaseModule.kt (which provided Room Database)
 * 
 * COMPARISON:
 * OLD DatabaseModule:
 *   - Provided AppDatabase
 *   - Provided UserDao
 * 
 * NEW FirebaseModule:
 *   - Provides FirebaseAuthService
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Provides Firebase Auth Service
     * 
     * REPLACES: provideAppDatabase() and provideUserDao()
     */
    @Provides
    @Singleton
    fun provideFirebaseAuthService(): FirebaseAuthService {
        return FirebaseAuthService()
    }

    /**
     * Provides AuthRepository implementation
     * 
     * CHANGED: Now injects FirebaseAuthService instead of UserDao
     * 
     * OLD: AuthRepositoryImpl(userDao)
     * NEW: AuthRepositoryImpl(firebaseAuthService)
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuthService: FirebaseAuthService
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuthService)
    }
}
