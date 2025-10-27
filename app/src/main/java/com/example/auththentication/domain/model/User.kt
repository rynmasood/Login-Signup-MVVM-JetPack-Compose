package com.example.auththentication.domain.model

/**
 * Domain Model - Represents User in business logic layer
 *
 * Why separate from UserEntity?
 * - Domain models are framework-independent (no Room annotations)
 * - Can be used across different data sources
 * - Separates business logic from data storage details
 */
data class User(val email: String, val fullName: String, val password: String)
