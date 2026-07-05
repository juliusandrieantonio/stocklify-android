package com.scalo.stocklify.feature.profile.ui

sealed class ProfileEvent {
    data object SignedOut : ProfileEvent()
    data class ShowError(val message: String) : ProfileEvent()
}
