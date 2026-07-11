package com.scalo.stocklify.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.data.database.dao.OrganizationMemberDao
import com.scalo.stocklify.core.model.enums.MembershipRole
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
    private val authRepository: AuthRepository,
    private val organizationMemberDao: OrganizationMemberDao
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEvent>()
    val event = _event.asSharedFlow()

    init {
        refresh()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.Refresh -> refresh()
            ProfileAction.SignOut -> signOut()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val user = authRepository.getCurrentUser()
            val role = user?.let {
                organizationMemberDao.getByUserId(it.id)?.role?.let(MembershipRole::from)
            }

            _state.update {
                it.copy(isLoading = false, email = user?.email, membershipRole = role)
            }
        }
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
