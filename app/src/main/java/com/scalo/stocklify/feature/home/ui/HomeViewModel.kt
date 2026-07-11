package com.scalo.stocklify.feature.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.sync.SyncManager
import com.scalo.stocklify.core.data.sync.SyncTableException
import com.scalo.stocklify.core.notification.SyncNotificationManager
import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    storeDao: StoreDao,
    authRepository: AuthRepository,
    private val syncManager: SyncManager,
    private val syncNotificationManager: SyncNotificationManager,
) : ViewModel() {

    val storeName = storeDao.getAll()
        .map { stores -> stores.firstOrNull()?.name.orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "",
        )

    val isAuthenticated = authRepository.isAuthenticated()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    fun sync() {
        if (_isSyncing.value) return

        viewModelScope.launch {
            _isSyncing.value = true
            syncNotificationManager.showSyncing()
            try {
                syncManager.incrementalSync()
                syncNotificationManager.showSuccess()
            } catch (e: Exception) {
                e.message?.let { Log.d("Sync Service", it) }
                syncNotificationManager.showError((e as? SyncTableException)?.tableName)
            } finally {
                _isSyncing.value = false
            }
        }
    }
}
