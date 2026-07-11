package com.scalo.stocklify.feature.premium.ui

sealed interface OrganizationAction {
    data class OrganizationNameChanged(val value: String) : OrganizationAction
    data object CreateOrganization : OrganizationAction
    data class InviteCodeChanged(val value: String) : OrganizationAction
    data object JoinOrganization : OrganizationAction
}
