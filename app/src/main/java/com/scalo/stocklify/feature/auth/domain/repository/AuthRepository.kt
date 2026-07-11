package com.scalo.stocklify.feature.auth.domain.repository

import com.scalo.stocklify.core.model.models.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun awaitInitialization()

    suspend fun signUp(email: String, password: String, metadata: Map<String, String?> = emptyMap()): AuthUser

    suspend fun signIn(email: String, password: String): AuthUser

    suspend fun signOut()

    suspend fun sendPasswordResetEmail(email: String)

    fun getCurrentUser(): AuthUser?

    suspend fun refreshCurrentUser(): AuthUser?

    suspend fun resendVerificationEmail(email: String)

    fun isAuthenticated(): Flow<Boolean>
}
