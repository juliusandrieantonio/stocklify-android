package com.scalo.stocklify.core.data.sync.push

import android.util.Log
import com.scalo.stocklify.core.data.database.dao.SyncOutboxDao
import com.scalo.stocklify.core.data.sync.api.RemoteSyncApi
import io.github.jan.supabase.auth.Auth
import kotlinx.datetime.Clock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds


@Singleton
class PushServiceImpl @Inject constructor(
    private val outboxDao: SyncOutboxDao,
    private val remoteSyncApi: RemoteSyncApi,
    private val auth: Auth
) : PushService {

    override suspend fun push() {
        val pending = outboxDao.getAll()
        if (pending.isEmpty()) return

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
            remoteSyncApi.pushChanges(pending)
        } catch (_: java.io.IOException) {
            pending.forEach { entry ->
                if (entry.retryCount + 1 >= MAX_ATTEMPTS) {
                    outboxDao.markFailed(entry.id, "retries exhausted")
                } else {
                    outboxDao.incrementRetry(entry.id)
                }
            }
            return
        }


        val acceptedSet = result.accepted.map { it.id }.toSet()
        val rejectedErrors = result.rejected.associate { it.id to it.message }
        pending.forEach { entry ->
            if (entry.id in acceptedSet) {
                outboxDao.delete(entry.id)
            } else {
                outboxDao.markFailed(entry.id, rejectedErrors[entry.id] ?: "rejected by server")
            }
        }
    }

    companion object {
        private const val MAX_ATTEMPTS = 5
    }
}
