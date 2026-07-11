package com.scalo.stocklify.feature.auth.ui

data class VerifyEmailUiState(
    val email: String = "",
    val isChecking: Boolean = false,
    val isResending: Boolean = false,
    val message: String? = null
)
