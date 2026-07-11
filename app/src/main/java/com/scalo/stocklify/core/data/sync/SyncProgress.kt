package com.scalo.stocklify.core.data.sync

import com.scalo.stocklify.core.data.sync.enums.SyncTask

sealed interface SyncProgress {

    data object Idle : SyncProgress

    data class Running(
        val task: SyncTask,
        val completed: Int,
        val total: Int
    ) : SyncProgress

    data object Finished : SyncProgress

    data class Error(
        val task: SyncTask,
        val tableName: String?,
        val throwable: Throwable
    ) : SyncProgress
}