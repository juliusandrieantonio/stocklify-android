package com.scalo.stocklify.core.data.sync.push

import android.util.Log
import com.scalo.stocklify.core.data.database.dao.SyncOutboxDao
import com.scalo.stocklify.core.data.database.entity.SyncOutboxEntity
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import com.scalo.stocklify.core.notification.SyncNotificationManager
import io.github.jan.supabase.auth.Auth
import kotlinx.datetime.Clock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds


@Singleton
class PushServiceImpl @Inject constructor(
    private val outboxDao: SyncOutboxDao,
    private val remoteSyncApi: RemoteSyncApi,
    private val auth: Auth,
    private val productImageSyncUploader: ProductImageSyncUploader,
    private val syncNotificationManager: SyncNotificationManager,
) : PushService {

    override suspend fun push() {
        val pending = outboxDao.getAll()
        if (pending.isEmpty()) return

        // Upload any still-local product images to Storage and rewrite the
        // outbox payloads to the resulting keys before pushing. Entries whose
        // upload fails are excluded from this batch and retried next sync.
        val toPush = productImageSyncUploader.uploadPendingImages(pending)
        if (toPush.isEmpty()) return

        // The background auto-refresh timer can race a token's actual expiry;
        // refresh proactively here so pushChanges never rides in on a stale JWT.
        val session = auth.currentSessionOrNull()
        if (session == null || session.expiresAt < Clock.System.now().plus(30.seconds)) {
            try {
                auth.refreshCurrentSession()
            } catch (e: Exception) {
                Log.e("SyncService", "refreshCurrentSession failed", e)
            }
        }

        val result = try {
            remoteSyncApi.pushChanges(toPush)
        } catch (_: java.io.IOException) {
            val exhausted = mutableListOf<SyncOutboxEntity>()
            toPush.forEach { entry ->
                if (entry.retryCount + 1 >= MAX_ATTEMPTS) {
                    outboxDao.markFailed(entry.id, "retries exhausted")
                    exhausted += entry
                } else {
                    outboxDao.incrementRetry(entry.id)
                }
            }
            notifyIfFailed(exhausted)
            return
        }


        val acceptedSet = result.accepted.map { it.id }.toSet()
        val rejectedErrors = result.rejected.associate { it.id to it.message }
        val rejected = mutableListOf<SyncOutboxEntity>()
        toPush.forEach { entry ->
            if (entry.id in acceptedSet) {
                outboxDao.delete(entry.id)
            } else {
                outboxDao.markFailed(entry.id, rejectedErrors[entry.id] ?: "rejected by server")
                rejected += entry
            }
        }
        notifyIfFailed(rejected)
    }

    // Only fires for entries that just became terminally failed (retries
    // exhausted or rejected by the server), never for ones still queued to
    // retry — a single transient hiccup shouldn't page the user.
    private fun notifyIfFailed(failed: List<SyncOutboxEntity>) {
        if (failed.isEmpty()) return
        val tableName = failed.map { it.tableName }.distinct().singleOrNull()
        syncNotificationManager.showError(tableName)
    }

    companion object {
        private const val MAX_ATTEMPTS = 5
    }
}
