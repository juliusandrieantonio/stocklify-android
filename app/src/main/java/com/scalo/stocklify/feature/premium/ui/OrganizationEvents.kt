package com.scalo.stocklify.feature.premium.ui

sealed class OrganizationEvent {
    data object OrganizationCreated : OrganizationEvent()
    data object OrganizationJoined : OrganizationEvent()
    data class ShowError(val message: String) : OrganizationEvent()
}
