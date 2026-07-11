package com.scalo.stocklify.feature.devtools.ui

data class DatabaseViewerUiState(
    val tables: List<String> = emptyList(),
    val selectedTable: String? = null,
    val query: String = "",
    val columns: List<String> = emptyList(),
    val rows: List<List<String?>> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
