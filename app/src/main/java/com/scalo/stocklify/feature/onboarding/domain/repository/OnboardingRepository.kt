package com.scalo.stocklify.feature.onboarding.domain.repository

interface OnboardingRepository {
    suspend fun hasStoreCached(): Boolean
    suspend fun hasCompletedOnboarding(): Boolean
    suspend fun completeOnboarding()
}