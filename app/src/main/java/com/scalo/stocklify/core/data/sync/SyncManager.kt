package com.scalo.stocklify.core.data.sync

import kotlinx.coroutines.flow.StateFlow

interface SyncManager {

    /**
     * Called when a user joins a store for the first time.
     * Downloads everything required for offline usage.
     */
    suspend fun initialSync()

    /**
     * Normal background synchronization.
     *
     * Push local changes then pull remote changes.
     */
    suspend fun incrementalSync()

    /**
     * Upload pending local changes.
     */
    suspend fun push()

    /**
     * Download server changes.
     */
    suspend fun pull()

    /**
     * Monitor Sync Progress
     */
    val progress: StateFlow<SyncProgress>

}