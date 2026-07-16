package com.scalo.stocklify.core.data.sync.push

import android.net.Uri
import android.util.Log
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.SyncOutboxDao
import com.scalo.stocklify.core.data.database.entity.SyncOutboxEntity
import com.scalo.stocklify.core.notification.SyncNotificationManager
import com.scalo.stocklify.core.util.constants.Tables
import com.scalo.stocklify.feature.products.domain.usecase.UploadProductImageUseCase
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri

/**
 * Runs immediately before a push. Any pending PRODUCTS outbox entry whose
 * image_url is still a local file:// URI gets its image uploaded to Supabase
 * Storage here (network is already assumed available at push time, unlike at
 * product-creation time), and the outbox payload + local row are rewritten to
 * the resulting storage key before the entry is sent to the server.
 *
 * Entries whose upload fails are dropped from this push's batch and retried
 * on the next sync cycle, up to MAX_ATTEMPTS — at which point the entry is
 * marked terminally failed (same as a rejected push) and the user is notified.
 * PushServiceImpl resets PUSH_FAILED entries back to PENDING at the start of
 * every push() run, so the next manual sync retries them again from there.
 */
@Singleton
class ProductImageSyncUploader @Inject constructor(
    private val outboxDao: SyncOutboxDao,
    private val productDao: ProductDao,
    private val storeDao: StoreDao,
    private val uploadProductImage: UploadProductImageUseCase,
    private val syncNotificationManager: SyncNotificationManager,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun uploadPendingImages(pending: List<SyncOutboxEntity>): List<SyncOutboxEntity> {
        if (pending.none { it.tableName == Tables.PRODUCTS }) return pending
        val failed = mutableListOf<SyncOutboxEntity>()
        val result = pending.mapNotNull { entry -> process(entry, failed) }
        notifyIfFailed(failed)
        return result
    }

    private suspend fun process(entry: SyncOutboxEntity, failed: MutableList<SyncOutboxEntity>): SyncOutboxEntity? {
        if (entry.tableName != Tables.PRODUCTS) return entry

        val payload = runCatching { json.parseToJsonElement(entry.payloadJson).jsonObject }
            .getOrNull() ?: return entry
        val imageUrl = payload["image_url"]?.jsonPrimitive?.contentOrNull ?: return entry
        val localUri = imageUrl.toUri()
        if (localUri.scheme != "file") return entry

        val productId = payload["id"]?.jsonPrimitive?.contentOrNull ?: return entry
        val organizationId = payload["organization_id"]?.jsonPrimitive?.contentOrNull ?: return entry
        val storeId = storeDao.getAll().first().firstOrNull()?.id ?: return entry

        val key = try {
            uploadProductImage(localUri, organizationId, storeId, productId)
        } catch (e: Exception) {
            Log.e(TAG, "Image upload failed for product $productId", e)
            if (entry.retryCount + 1 >= MAX_ATTEMPTS) {
                outboxDao.markFailed(entry.id, "image upload failed: ${e.message ?: "unknown error"}")
                failed += entry
            } else {
                outboxDao.incrementRetry(entry.id)
            }
            return null
        }

        val updatedPayload = JsonObject(payload.toMutableMap().apply { put("image_url", JsonPrimitive(key)) })
        val updatedPayloadJson = json.encodeToString(JsonObject.serializer(), updatedPayload)

        outboxDao.updatePayload(entry.id, updatedPayloadJson)
        productDao.updateImageUrl(productId, key)

        return entry.copy(payloadJson = updatedPayloadJson)
    }

    private fun notifyIfFailed(failed: List<SyncOutboxEntity>) {
        if (failed.isEmpty()) return
        val tableName = failed.map { it.tableName }.distinct().singleOrNull()
        syncNotificationManager.showError(tableName)
    }

    companion object {
        private const val TAG = "ProductImageSyncUploader"
        private const val MAX_ATTEMPTS = 5
    }
}
