package com.scalo.stocklify.feature.auth.data.repository

import android.util.Log
import com.scalo.stocklify.core.model.models.AuthUser
import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth
) : AuthRepository {

    override suspend fun awaitInitialization() {
        auth.awaitInitialization()
    }

    override suspend fun signUp(
        email: String,
        password: String,
        metadata: Map<String, String?>
    ): AuthUser {
        try {
            val user = auth.signUpWith(Email) {
                this.email = email
                this.password = password

                if (metadata.isNotEmpty()) {
                    data = buildJsonObject {
                        metadata.forEach { (key, value) ->
                            put(key, value)
                        }
                    }
                }
            }

            return (user ?: auth.currentUserOrNull())
                ?.toDomain()
                ?: error("Sign up succeeded but no user session was returned")

        } catch (e: Exception) {
            Log.e("SignUp", "Signup failed", e)
            throw e
        }
    }

    override suspend fun signIn(email: String, password: String): AuthUser {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        return requireNotNull(getCurrentUser()) { "Sign in succeeded but no user session was returned" }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        auth.resetPasswordForEmail(email)
    }

    override fun getCurrentUser(): AuthUser? {
        return auth.currentUserOrNull()?.toDomain()
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return auth.sessionStatus.map { it is SessionStatus.Authenticated }
    }
}

private fun UserInfo.toDomain() = AuthUser(id = id, email = email)
