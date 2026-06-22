package com.scalo.stocklify.feature.stores.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.models.Store
import com.scalo.stocklify.feature.stores.domain.repository.StoreRepository
import com.scalo.stocklify.feature.stores.domain.usecase.CreateStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val createStoreUseCase: CreateStoreUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StoreUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<StoreEvent>()
    val event = _event.asSharedFlow()

    fun createStore() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                val store = Store(
                    storeCode = "AUTO-GEN",
                    name = "TEST",
                    storeTypeId = null
                )

                createStoreUseCase(store)

                _event.emit(StoreEvent.StoreCreated)

            } catch (e: Exception) {
                _event.emit(StoreEvent.ShowError(e.message ?: "Error"))
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}