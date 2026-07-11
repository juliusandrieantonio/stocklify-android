package com.scalo.stocklify.feature.onboarding.domain.usecase

import com.scalo.stocklify.feature.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

class CompleteWelcomeScreenUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke() = repository.completeWelcomeScreen()
}