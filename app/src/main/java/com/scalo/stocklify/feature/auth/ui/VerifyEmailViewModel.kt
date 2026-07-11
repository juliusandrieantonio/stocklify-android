package com.scalo.stocklify.feature.auth.ui

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

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        VerifyEmailUiState(email = authRepository.getCurrentUser()?.email.orEmpty())
    )
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<VerifyEmailEvent>()
    val event = _event.asSharedFlow()

    fun checkVerification() {
        viewModelScope.launch {
            _state.update { it.copy(isChecking = true, message = null) }
            try {
                val user = authRepository.refreshCurrentUser()
                if (user?.isEmailVerified == true) {
                    _event.emit(VerifyEmailEvent.Verified)
                } else {
                    _state.update { it.copy(message = "Your email isn't verified yet. Please check your inbox.") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(message = e.toVerifyEmailErrorMessage()) }
            } finally {
                _state.update { it.copy(isChecking = false) }
            }
        }
    }

    fun resendEmail() {
        val email = _state.value.email
        if (email.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isResending = true, message = null) }
            try {
                authRepository.resendVerificationEmail(email)
                _state.update { it.copy(message = "Verification email sent to $email") }
            } catch (e: Exception) {
                _state.update { it.copy(message = e.toVerifyEmailErrorMessage()) }
            } finally {
                _state.update { it.copy(isResending = false) }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _event.emit(VerifyEmailEvent.SignedOut)
        }
    }
}
