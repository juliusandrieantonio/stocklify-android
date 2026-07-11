package com.scalo.stocklify.feature.premium.ui

data class OrganizationUiState(
    val organizationName: String = "",
    val organizationNameError: String? = null,
    val inviteCode: String = "",
    val inviteCodeError: String? = null,
    val isLoading: Boolean = false
)
