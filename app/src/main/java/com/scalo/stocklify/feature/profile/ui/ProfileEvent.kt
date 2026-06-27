package com.scalo.stocklify.feature.profile.ui

sealed class ProfileEvent {
    data class ShowError(val message: String) : ProfileEvent()
}
