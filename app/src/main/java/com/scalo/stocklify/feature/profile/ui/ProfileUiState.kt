package com.scalo.stocklify.feature.profile.ui

import com.scalo.stocklify.core.model.enums.MembershipRole

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val email: String? = null,
    val membershipRole: MembershipRole? = null
)
