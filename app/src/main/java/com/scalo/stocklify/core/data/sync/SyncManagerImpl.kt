package com.scalo.stocklify.core.data.sync

import com.scalo.stocklify.core.data.sync.enums.SyncTask
import com.scalo.stocklify.core.data.sync.pull.PullService
import com.scalo.stocklify.core.data.sync.push.PushService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManagerImpl @Inject constructor(
    private val pushService: PushService,
    private val pullService: PullService
) : SyncManager {

    private val _progress =
        MutableStateFlow<SyncProgress>(SyncProgress.Idle)

    override val progress =
        _progress.asStateFlow()

    private var currentProgress: SyncProgress = SyncProgress.Idle

    override suspend fun initialSync() {
        pullService
            .initialPull()
            .onEach { progress ->
                currentProgress = progress
                _progress.value = progress
            }
            .catch { exception ->
                val task = (exception as? SyncTableException)?.task
                    ?: (currentProgress as? SyncProgress.Running)?.task
                    ?: SyncTask.UNKNOWN

                _progress.value = SyncProgress.Error(
                    task = task,
                    tableName = (exception as? SyncTableException)?.tableName,
                    throwable = exception
                )
            }
            .collect()
    }

    override suspend fun incrementalSync() {
        push()
        pull()
    }

    override suspend fun push() {
        pushService.push()
    }

    override suspend fun pull() {
        pullService.pull()
    }
}