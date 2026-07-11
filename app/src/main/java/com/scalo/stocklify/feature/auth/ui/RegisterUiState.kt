package com.scalo.stocklify.feature.auth.ui

data class RegisterUiState(
    val firstName: String = "",
    val middleName: String = "",
    val surname: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstNameError: String? = null,
    val surnameError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false
)
