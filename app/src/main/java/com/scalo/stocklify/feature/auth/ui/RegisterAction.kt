package com.scalo.stocklify.feature.auth.ui

sealed interface RegisterAction {
    data class FirstNameChanged(val value: String) : RegisterAction
    data class MiddleNameChanged(val value: String) : RegisterAction
    data class SurnameChanged(val value: String) : RegisterAction
    data class PhoneChanged(val value: String) : RegisterAction
    data class EmailChanged(val value: String) : RegisterAction
    data class PasswordChanged(val value: String) : RegisterAction
    data class ConfirmPasswordChanged(val value: String) : RegisterAction
    data object Submit : RegisterAction
}
