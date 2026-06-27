package com.scalo.stocklify.feature.salesmode.ui

sealed interface SalesModeAction {
    data object Refresh : SalesModeAction
    data class SelectCategory(val categoryId: Long?) : SalesModeAction
}
