package com.scalo.stocklify.feature.auth.ui

sealed class VerifyEmailEvent {
    data object Verified : VerifyEmailEvent()
    data object SignedOut : VerifyEmailEvent()
}
