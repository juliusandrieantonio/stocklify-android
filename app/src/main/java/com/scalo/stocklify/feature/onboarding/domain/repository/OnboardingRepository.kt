package com.scalo.stocklify.feature.onboarding.domain.repository

interface OnboardingRepository {
    suspend fun hasStoreCached(): Boolean
    suspend fun hasOrganizationCached(): Boolean
    suspend fun hasCompletedOnboarding(): Boolean
    suspend fun hasCompletedWelcomeScreen(): Boolean
    suspend fun completeOnboarding()
    suspend fun completeWelcomeScreen()
    suspend fun setStoreCached(hasStore: Boolean)
    suspend fun setOrganizationCached(hasOrganization: Boolean)
}