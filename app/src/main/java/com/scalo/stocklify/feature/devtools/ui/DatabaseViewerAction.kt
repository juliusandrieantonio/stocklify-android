package com.scalo.stocklify.feature.devtools.ui

sealed interface DatabaseViewerAction {
    data class SelectTable(val tableName: String) : DatabaseViewerAction
    data class QueryChanged(val value: String) : DatabaseViewerAction
    data object RunQuery : DatabaseViewerAction
}
