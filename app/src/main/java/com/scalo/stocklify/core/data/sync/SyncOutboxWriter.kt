package com.scalo.stocklify.core.data.sync

import com.scalo.stocklify.core.data.database.dao.SyncOutboxDao
import com.scalo.stocklify.core.data.database.entity.SyncOutboxEntity
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.util.constants.OutboxOperation
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class SyncOutboxWriter @Inject constructor(
    @PublishedApi internal val outboxDao: SyncOutboxDao
) {
    suspend inline fun <reified T> enqueueInsert(tableName: String, payload: T) {
        outboxDao.enqueue(
            SyncOutboxEntity(
                id = UUID.randomUUID().toString(),
                tableName = tableName,
                operation = OutboxOperation.INSERT,
                payloadJson = Json.encodeToString(payload),
                createdAt = System.currentTimeMillis(),
                status = SyncStatus.PENDING
            )
        )
    }
}
