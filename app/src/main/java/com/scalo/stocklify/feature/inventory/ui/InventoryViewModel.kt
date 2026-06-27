package com.scalo.stocklify.feature.inventory.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(InventoryUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<InventoryEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: InventoryAction) {
        when (action) {
            InventoryAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        _state.update { it.copy(isLoading = true, error = null) }
        // TODO: load inventory data
        _state.update { it.copy(isLoading = false) }
    }
}
