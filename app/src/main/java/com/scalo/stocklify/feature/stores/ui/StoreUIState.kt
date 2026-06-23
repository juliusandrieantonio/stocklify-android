package com.scalo.stocklify.feature.stores.ui

import com.scalo.stocklify.core.model.models.StoreType

data class StoreUiState(
    val storeName: String = "",
    val storeTypes: List<StoreType> = emptyList(),
    val selectedStoreType: StoreType? = null,
    val storeNameError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)