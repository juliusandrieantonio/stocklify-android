package com.scalo.stocklify.feature.profile.ui

sealed interface ProfileAction {
    data object Refresh : ProfileAction
    data object SignOut : ProfileAction
}
