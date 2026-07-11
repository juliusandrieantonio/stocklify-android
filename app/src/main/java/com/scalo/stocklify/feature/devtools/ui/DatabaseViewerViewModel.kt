package com.scalo.stocklify.feature.devtools.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.scalo.stocklify.core.data.database.dao.DevToolsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEFAULT_ROW_LIMIT = 200

@HiltViewModel
class DatabaseViewerViewModel @Inject constructor(
    private val devToolsDao: DevToolsDao
) : ViewModel() {

    private val _state = MutableStateFlow(DatabaseViewerUiState())
    val state = _state.asStateFlow()

    init {
        loadTables()
    }

    fun onAction(action: DatabaseViewerAction) {
        when (action) {
            is DatabaseViewerAction.SelectTable -> selectTable(action.tableName)
            is DatabaseViewerAction.QueryChanged -> _state.update { it.copy(query = action.value) }
            DatabaseViewerAction.RunQuery -> runQuery(_state.value.query)
        }
    }

    private fun loadTables() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cursor = devToolsDao.rawQuery(
                    SimpleSQLiteQuery(
                        "SELECT name FROM sqlite_master WHERE type = 'table' " +
                            "AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'room_master_table' ORDER BY name"
                    )
                )
                val tables = buildList {
                    cursor.use {
                        while (it.moveToNext()) add(it.getString(0))
                    }
                }
                _state.update { it.copy(tables = tables) }
                tables.firstOrNull()?.let { selectTable(it) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to load tables") }
            }
        }
    }

    private fun selectTable(tableName: String) {
        val query = "SELECT * FROM $tableName LIMIT $DEFAULT_ROW_LIMIT"
        _state.update { it.copy(selectedTable = tableName, query = query) }
        runQuery(query)
    }

    private fun runQuery(query: String) {
        val trimmed = query.trim()
        if (!trimmed.startsWith("select", ignoreCase = true) && !trimmed.startsWith("pragma", ignoreCase = true)) {
            _state.update { it.copy(error = "Only SELECT/PRAGMA queries are allowed") }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val cursor = devToolsDao.rawQuery(SimpleSQLiteQuery(trimmed))
                val columns: List<String>
                val rows: List<List<String?>>
                cursor.use {
                    columns = it.columnNames.toList()
                    rows = buildList {
                        while (it.moveToNext()) {
                            add((0 until it.columnCount).map { i -> it.getString(i) })
                        }
                    }
                }
                _state.update { it.copy(columns = columns, rows = rows, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Query failed", isLoading = false) }
            }
        }
    }
}
