package com.scalo.stocklify.feature.onboarding.ui

import com.scalo.stocklify.feature.onboarding.model.OnBoardingPage

data class OnboardingUiState(
    val pages: List<OnBoardingPage> = emptyList(),
    val isCompleted: Boolean = false,
    val isLoading: Boolean = false
)