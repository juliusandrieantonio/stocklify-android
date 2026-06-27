package com.scalo.stocklify.feature.records.ui

sealed class RecordsEvent {
    data class ShowError(val message: String) : RecordsEvent()
}
