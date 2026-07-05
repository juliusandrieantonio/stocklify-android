package com.scalo.stocklify.feature.profile.ui

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
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.Refresh -> refresh()
            ProfileAction.SignOut -> signOut()
        }
    }

    private fun refresh() {
        _state.update { it.copy(isLoading = true, error = null) }
        // TODO: load profile data
        _state.update { it.copy(isLoading = false) }
    }

    private fun signOut() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                authRepository.signOut()
                _event.emit(ProfileEvent.SignedOut)
            } catch (e: Exception) {
                _event.emit(ProfileEvent.ShowError(e.message ?: "Unable to sign out"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
