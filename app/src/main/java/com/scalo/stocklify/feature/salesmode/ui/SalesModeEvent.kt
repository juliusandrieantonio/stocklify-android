package com.scalo.stocklify.feature.salesmode.ui

sealed class SalesModeEvent {
    data class ShowError(val message: String) : SalesModeEvent()
}
