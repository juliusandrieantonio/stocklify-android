package com.scalo.stocklify.feature.auth.ui

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import com.scalo.stocklify.feature.onboarding.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event = _event.asSharedFlow()

    private fun validate(): Boolean {
        val current = _state.value
        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(current.email.trim()).matches()) {
            _state.update { it.copy(emailError = "Enter a valid email") }
            isValid = false
        }

        if (current.password.isBlank()) {
            _state.update { it.copy(passwordError = "Password is required") }
            isValid = false
        }

        return isValid
    }

    private fun login() {
        if (!validate()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val current = _state.value
                authRepository.signIn(current.email.trim(), current.password)
                _event.emit(LoginEvent.LoggedIn(onboardingRepository.hasStoreCached()))
            } catch (e: Exception) {
                _event.emit(LoginEvent.ShowError(e.message ?: "Unable to sign in"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.EmailChanged ->
                _state.update { it.copy(email = action.value, emailError = null) }

            is LoginAction.PasswordChanged ->
                _state.update { it.copy(password = action.value, passwordError = null) }

            LoginAction.Submit ->
                login()
        }
    }
}
