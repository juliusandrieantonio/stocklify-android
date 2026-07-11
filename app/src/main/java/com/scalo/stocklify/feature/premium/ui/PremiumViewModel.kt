package com.scalo.stocklify.feature.premium.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.feature.premium.domain.PremiumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val premiumUseCase: PremiumUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OrganizationUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<OrganizationEvent>()
    val event = _event.asSharedFlow()

    private fun validateOrganizationName(): Boolean {
        val current = _state.value
        var isValid = true

        if (current.organizationName.isBlank()) {
            _state.update { it.copy(organizationNameError = "Organization name is required") }
            isValid = false
        }

        return isValid
    }

    private fun validateInviteCode(): Boolean {
        val current = _state.value
        var isValid = true

        if (current.inviteCode.isBlank()) {
            _state.update { it.copy(inviteCodeError = "Invitation code is required") }
            isValid = false
        }

        return isValid
    }

    private fun createOrganization() {
        if (!validateOrganizationName()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                premiumUseCase.createOrganization(_state.value.organizationName.trim())
                _event.emit(OrganizationEvent.OrganizationCreated)
            } catch (e: Exception) {
                _event.emit(OrganizationEvent.ShowError(e.message ?: "Error"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun joinOrganization() {
        if (!validateInviteCode()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                premiumUseCase.joinOrganization(_state.value.inviteCode.trim())
                _event.emit(OrganizationEvent.OrganizationJoined)
            } catch (e: Exception) {
                _event.emit(OrganizationEvent.ShowError(e.message ?: "Error"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAction(action: OrganizationAction) {
        when (action) {
            is OrganizationAction.OrganizationNameChanged ->
                _state.update { it.copy(organizationName = action.value, organizationNameError = null) }

            OrganizationAction.CreateOrganization ->
                createOrganization()

            is OrganizationAction.InviteCodeChanged ->
                _state.update { it.copy(inviteCode = action.value, inviteCodeError = null) }

            OrganizationAction.JoinOrganization ->
                joinOrganization()
        }
    }
}
