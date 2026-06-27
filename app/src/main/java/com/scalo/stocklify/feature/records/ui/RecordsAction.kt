package com.scalo.stocklify.feature.records.ui

sealed interface RecordsAction {
    data object Refresh : RecordsAction
}
