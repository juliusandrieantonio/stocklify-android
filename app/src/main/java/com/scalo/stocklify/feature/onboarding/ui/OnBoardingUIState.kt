package com.scalo.stocklify.feature.onboarding.ui

data class OnboardingUiState(
    val pages: List<OnBoardingPage> = emptyList(),
    val isCompleted: Boolean = false,
    val isLoading: Boolean = false
)