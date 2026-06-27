package com.scalo.stocklify.feature.profile.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        _state.update { it.copy(isLoading = true, error = null) }
        // TODO: load profile data
        _state.update { it.copy(isLoading = false) }
    }
}
