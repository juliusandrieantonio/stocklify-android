package com.scalo.stocklify.feature.auth.ui

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_PASSWORD_LENGTH = 6

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<RegisterEvent>()
    val event = _event.asSharedFlow()

    private fun validate(): Boolean {
        val current = _state.value
        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(current.email.trim()).matches()) {
            _state.update { it.copy(emailError = "Enter a valid email") }
            isValid = false
        }

        if (current.password.length < MIN_PASSWORD_LENGTH) {
            _state.update { it.copy(passwordError = "Password must be at least $MIN_PASSWORD_LENGTH characters") }
            isValid = false
        }

        if (current.confirmPassword != current.password) {
            _state.update { it.copy(confirmPasswordError = "Passwords do not match") }
            isValid = false
        }

        return isValid
    }

    private fun register() {
        if (!validate()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val current = _state.value
                authRepository.signUp(current.email.trim(), current.password)
                _event.emit(RegisterEvent.Registered)
            } catch (e: Exception) {
                _event.emit(RegisterEvent.ShowError(e.message ?: "Unable to sign up"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.EmailChanged ->
                _state.update { it.copy(email = action.value, emailError = null) }

            is RegisterAction.PasswordChanged ->
                _state.update { it.copy(password = action.value, passwordError = null) }

            is RegisterAction.ConfirmPasswordChanged ->
                _state.update { it.copy(confirmPassword = action.value, confirmPasswordError = null) }

            RegisterAction.Submit ->
                register()
        }
    }
}
