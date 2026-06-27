package com.scalo.stocklify.feature.inventory.ui

sealed class InventoryEvent {
    data class ShowError(val message: String) : InventoryEvent()
}
