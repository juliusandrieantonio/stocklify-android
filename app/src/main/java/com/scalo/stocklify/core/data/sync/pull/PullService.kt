package com.scalo.stocklify.core.data.sync.pull

import com.scalo.stocklify.core.data.sync.SyncProgress
import kotlinx.coroutines.flow.Flow

interface PullService {

    fun initialPull(): Flow<SyncProgress>

    suspend fun pull()
}