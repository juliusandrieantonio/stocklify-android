package com.scalo.stocklify.feature.products.domain.usecase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.util.constants.Buckets
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import androidx.core.net.toUri

/**
 * Resolves a product's image to a local [File], downloading it from Supabase
 * Storage on first access and caching it on device thereafter. Called lazily
 * from the UI when an image is actually about to be shown, not eagerly on pull.
 */
class GetProductImageFileUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storage: Storage,
    private val productDao: ProductDao,
) {

    suspend operator fun invoke(productId: String, imageUrl: String?): File? = withContext(Dispatchers.IO) {
        if (imageUrl.isNullOrBlank()) return@withContext null

        // Own-device product whose image hasn't been uploaded (and therefore
        // hasn't round-tripped through the server) yet — use the file as-is.
        val localUri = imageUrl.toUri()
        if (localUri.scheme == "file") {
            val file = localUri.path?.let { File(it) }
            return@withContext file?.takeIf { it.exists() }
        }

        val entity = productDao.getById(productId) ?: return@withContext null
        val cachedPath = entity.localImagePath
        if (entity.cachedImageKey == imageUrl && cachedPath != null) {
            val cachedFile = File(cachedPath)
            if (cachedFile.exists()) return@withContext cachedFile
        }

        try {
            val bytes = storage.from(Buckets.PRODUCT_IMAGES).downloadAuthenticated(imageUrl)
            val dir = File(context.filesDir, "product_image_cache").apply { mkdirs() }
            val file = File(dir, "$productId.webp")
            file.writeBytes(bytes)
            productDao.updateLocalImageCache(productId, file.absolutePath, imageUrl)
            file
        } catch (e: Exception) {
            Log.e(TAG, "Failed to download product image for $productId", e)
            null
        }
    }

    companion object {
        private const val TAG = "GetProductImageFileUseCase"
    }
}
