package com.scalo.stocklify.feature.auth.ui

sealed class LoginEvent {
    class LoggedIn() : LoginEvent()
    data class ShowError(val message: String) : LoginEvent()
}
