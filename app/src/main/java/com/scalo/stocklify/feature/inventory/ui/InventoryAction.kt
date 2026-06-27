package com.scalo.stocklify.feature.inventory.ui

sealed interface InventoryAction {
    data object Refresh : InventoryAction
}
