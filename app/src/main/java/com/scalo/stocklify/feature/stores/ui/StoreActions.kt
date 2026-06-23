package com.scalo.stocklify.feature.stores.ui

import com.scalo.stocklify.core.model.models.StoreType

sealed interface StoreAction {
    data class StoreNameChanged(val value: String) : StoreAction
    data class StoreTypeChanged(val storeType: StoreType) : StoreAction
    data object CreateStore : StoreAction
}