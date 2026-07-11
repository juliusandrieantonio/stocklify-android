package com.scalo.stocklify.core.data.sync

import com.scalo.stocklify.core.data.sync.enums.SyncTask

/**
 * Wraps whatever failed while syncing one table, so callers can report which
 * table broke without needing to inspect (or surface) the underlying cause.
 */
class SyncTableException(
    val task: SyncTask,
    val tableName: String,
    cause: Throwable
) : Exception(cause)
