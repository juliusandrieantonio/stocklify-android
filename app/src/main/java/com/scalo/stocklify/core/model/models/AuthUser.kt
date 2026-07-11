package com.scalo.stocklify.core.model.models

data class AuthUser(
    val id: String,
    val email: String?,
    val isEmailVerified: Boolean = false
)
