package com.scalo.stocklify.feature.preparing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.data.sync.SyncManager
import com.scalo.stocklify.core.data.sync.SyncProgress
import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.helper.MembershipLocalDataSource
import com.scalo.stocklify.core.notification.SyncNotificationManager
import com.scalo.stocklify.core.util.snakeToTitleCase
import com.scalo.stocklify.feature.onboarding.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreparingWorkUiState(
    val progress: Float = 0f,
    val statusMessage: String = "Please wait while we get your stores..."
)

sealed interface PreparingWorkEvent {
    data class Ready(val hasOrganizationCached: Boolean, val hasStoreCached: Boolean) : PreparingWorkEvent
}

@HiltViewModel
class PreparingWorkViewModel @Inject constructor(
    private val syncManager: SyncManager,
    private val membership: MembershipLocalDataSource,
    private val onboardingRepository: OnboardingRepository,
    private val syncNotificationManager: SyncNotificationManager
) : ViewModel() {

    private val _state = MutableStateFlow(PreparingWorkUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<PreparingWorkEvent>()
    val event = _event.asSharedFlow()

    private var started = false

    fun start() {
        if (started) return
        started = true

        viewModelScope.launch {
            syncManager.progress.collect { progress ->
                when (progress) {
                    is SyncProgress.Idle -> Unit

                    is SyncProgress.Running -> {
                        if (progress.completed == 0 && progress.task == SyncTask.USER) {
                            syncNotificationManager.showSyncing()
                        }
                        _state.update {
                            it.copy(
                                progress = if (progress.total > 0) {
                                    progress.completed / progress.total.toFloat()
                                } else 0f,
                                statusMessage = "Syncing ${progress.task.name.snakeToTitleCase()}..."
                            )
                        }
                    }

                    is SyncProgress.Finished -> {
                        _state.update { it.copy(progress = 1f) }
                        syncNotificationManager.showSuccess()
                    }

                    is SyncProgress.Error -> syncNotificationManager.showError(progress.tableName)
                }
            }
        }

        viewModelScope.launch {
            // Errors are swallowed into SyncProgress.Error by SyncManagerImpl, so this
            // always completes once the attempt is over, success or not — the
            // organization/store cache below then reflects whatever ended up in Room.
            syncManager.initialSync()

            val hasOrganization = membership.myOrganizationIds().isNotEmpty()
            val hasStore = membership.myStoreIds().isNotEmpty()

            onboardingRepository.setOrganizationCached(hasOrganization)
            onboardingRepository.setStoreCached(hasStore)

            _event.emit(PreparingWorkEvent.Ready(hasOrganization, hasStore))
        }
    }
}
