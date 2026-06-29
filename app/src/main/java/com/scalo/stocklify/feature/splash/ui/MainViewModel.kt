package com.scalo.stocklify.feature.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.util.Screen
import com.scalo.stocklify.feature.onboarding.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val onboardingRepo: OnboardingRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    init {
        viewModelScope.launch {
            _isLoading.value = true

            val hasCompletedOnboarding =
                onboardingRepo.hasCompletedOnboarding()

            val hasStoreCached =
                onboardingRepo.hasStoreCached()

            _startDestination.value = when {

                !hasCompletedOnboarding ->
                    Screen.Onboarding.route

                !hasStoreCached ->
                    Screen.CreateJoinStore.route

                else ->
                    "home_flow"
            }
            _isLoading.value = false
        }
    }
}