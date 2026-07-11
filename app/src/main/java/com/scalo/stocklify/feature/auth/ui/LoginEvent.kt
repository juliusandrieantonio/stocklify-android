package com.scalo.stocklify.feature.auth.ui

sealed class LoginEvent {
    data class LoggedIn(val isEmailVerified: Boolean) : LoginEvent()
    data class ShowError(val message: String) : LoginEvent()
}
