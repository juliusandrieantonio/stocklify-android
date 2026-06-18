package com.scalo.stocklify.feature.onboarding.ui

import com.scalo.stocklify.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OnboardingUiState(
            pages = listOf(
                OnBoardingPage(
                    title = "Welcome to Stocklify",
                    subTitle = "Your smart sales and inventory assistant.",
                    image = R.drawable.onboarding_1,
                    contentText = "Track products, manage inventory, and complete sales in just a few taps.",
                ),
                OnBoardingPage(
                    title = "Sell in Seconds",
                    subTitle = "Fast, simple transactions.",
                    image = R.drawable.onboarding_2,
                    contentText = "Tap products to instantly view prices, add items to a sale, and calculate totals automatically.",
                ),
                OnBoardingPage(
                    title = "Ready to Grow Your Business?",
                    subTitle = "Everything you need in one place.",
                    image = R.drawable.onboarding_3,
                    contentText = "Monitor inventory, track sales, and make smarter business decisions with Stocklify AI.",
                )
            )
        )
    )

    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun completeOnboarding() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            completeOnboardingUseCase()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isCompleted = true
                )
            }
        }
    }
}