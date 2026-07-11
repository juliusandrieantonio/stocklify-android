package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.scalo.stocklify.core.data.database.entity.SyncOutboxEntity

@Dao
interface SyncOutboxDao : BaseDao<SyncOutboxEntity> {

    @Query("SELECT * FROM sync_outbox ORDER BY created_at ASC")
    suspend fun getAll(): List<SyncOutboxEntity>

    @Upsert
    suspend fun enqueue(entry: SyncOutboxEntity)

    @Query("DELETE FROM sync_outbox WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE sync_outbox SET retry_count = retry_count + 1 WHERE id = :id")
    suspend fun incrementRetry(id: String)

    @Query("UPDATE sync_outbox SET status = 'PUSH_FAILED', last_error = :reason WHERE id = :id")
    suspend fun markFailed(id: String, reason: String)

    /** For a manual "retry" tap in a sync-issues screen. */
    @Query("UPDATE sync_outbox SET status = 'PENDING', retry_count = 0, last_error = NULL WHERE id = :id")
    suspend fun resetForRetry(id: String)
}
