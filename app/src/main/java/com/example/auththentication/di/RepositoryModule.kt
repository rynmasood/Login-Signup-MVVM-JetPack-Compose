package com.example.auththentication.di

import com.example.auththentication.data.repository.AuthRepositoryImpl
import com.example.auththentication.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository Module - Binds repository implementations to interfaces
 *
 * Why @Binds instead of @Provides?
 * - @Binds is more efficient for simple interface-to-implementation binding
 * - Generates less code
 * - Used when you just need to tell Hilt which implementation to use for an interface
 *
 * Why abstract class?
 * - @Binds requires the module to be abstract
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Tells Hilt: When someone needs AuthRepository, provide AuthRepositoryImpl
     *
     * How it works:
     * - AuthRepositoryImpl is injected with UserDao (from DatabaseModule)
     * - Hilt creates AuthRepositoryImpl automatically
     * - Returns it as AuthRepository interface
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}
