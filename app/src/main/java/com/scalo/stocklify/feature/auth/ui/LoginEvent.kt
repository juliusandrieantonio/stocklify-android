package com.scalo.stocklify.feature.auth.ui

sealed class LoginEvent {
    data class LoggedIn(val hasStoreCached: Boolean) : LoginEvent()
    data class ShowError(val message: String) : LoginEvent()
}
