package com.scalo.stocklify.feature.stores.ui

sealed class StoreEvent {
    data object StoreCreated : StoreEvent()
    data class ShowError(val message: String) : StoreEvent()
}