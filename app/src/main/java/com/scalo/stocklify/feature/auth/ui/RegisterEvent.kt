package com.scalo.stocklify.feature.auth.ui

sealed class RegisterEvent {
    data object Registered : RegisterEvent()
    data class ShowError(val message: String) : RegisterEvent()
}
