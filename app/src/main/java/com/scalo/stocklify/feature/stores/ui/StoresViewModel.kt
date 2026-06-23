package com.scalo.stocklify.feature.stores.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.model.models.Store
import com.scalo.stocklify.feature.stores.domain.usecase.CreateStoreUseCase
import com.scalo.stocklify.feature.stores.domain.usecase.GetStoreTypesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val createStoreUseCase: CreateStoreUseCase,
    private val getStoreTypesUseCase: GetStoreTypesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StoreUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<StoreEvent>()
    val event = _event.asSharedFlow()

    init {
        loadStoreTypes()
    }

    private fun loadStoreTypes() {
        getStoreTypesUseCase()
            .onEach { types -> _state.update { it.copy(storeTypes = types) } }
            .launchIn(viewModelScope)
    }

    private fun validate(): Boolean {
        val current = _state.value
        var isValid = true

        if (current.storeName.isBlank()) {
            _state.update { it.copy(storeNameError = "Store name is required") }
            isValid = false
        }

        return isValid
    }

    private fun createStore() {
        if (!validate()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val current = _state.value
                val store = Store(
                    storeCode = "AUTO-GEN",
                    name = current.storeName.trim(),
                    storeTypeId = current.selectedStoreType?.id
                )

                createStoreUseCase(store)

                _event.emit(StoreEvent.StoreCreated)

            } catch (e: Exception) {
                _event.emit(StoreEvent.ShowError(e.message ?: "Error"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAction(action: StoreAction) {
        when (action) {
            is StoreAction.StoreNameChanged ->
                _state.update { it.copy(storeName = action.value, storeNameError = null) }

            is StoreAction.StoreTypeChanged ->
                _state.update { it.copy(selectedStoreType = action.storeType) }

            StoreAction.CreateStore ->
                createStore()
        }
    }
}