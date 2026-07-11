package com.scalo.stocklify.feature.splash.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.util.Screen
import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import com.scalo.stocklify.feature.onboarding.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val onboardingRepo: OnboardingRepository,
    private val authRepository: AuthRepository
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

            val hasCompletedWelcomeScreen =
                onboardingRepo.hasCompletedWelcomeScreen()

            authRepository.awaitInitialization()
            val isAuthenticated = authRepository.getCurrentUser() != null
            val currentUser = if (isAuthenticated) authRepository.refreshCurrentUser() else null
            val isEmailVerified = currentUser?.isEmailVerified == true

            val hasOrganizationCached =
                onboardingRepo.hasOrganizationCached()

            val hasStoreCached =
                onboardingRepo.hasStoreCached()

            _startDestination.value = when {

                !hasCompletedOnboarding ->
                    Screen.Onboarding.route

                !hasCompletedWelcomeScreen ->
                    Screen.WelcomePage.route

                !isAuthenticated ->
                    Screen.Login.route

                !isEmailVerified ->
                    Screen.VerifyEmail.route

                !hasOrganizationCached ->
                    Screen.CreateOrganization.route

//                !hasStoreCached ->
//                    Screen.CreateJoinStore.route

                else ->
                    "home_flow"
            }
            _isLoading.value = false
        }
    }
}