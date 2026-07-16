package com.scalo.stocklify.feature.products.domain.usecase

import android.content.Context
import android.net.Uri
import com.scalo.stocklify.core.util.ImageCompressor
import com.scalo.stocklify.core.util.constants.Buckets
import com.scalo.stocklify.core.util.constants.ImageUploadConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Compresses a locally picked product image and uploads it to Supabase Storage,
 * returning the storage key (e.g. "organizations/{orgId}/stores/{storeId}/products/{productId}_v1.webp")
 * to be stored on the product record in place of the local file URI.
 */
class UploadProductImageUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storage: Storage,
) {

    suspend operator fun invoke(
        localImageUri: Uri,
        organizationId: String,
        storeId: String,
        productId: String,
    ): String = withContext(Dispatchers.IO) {
        val bytes = ImageCompressor.compressToWebp(
            context = context,
            uri = localImageUri,
            maxDimensionPx = ImageUploadConfig.MAX_DIMENSION_PX,
            quality = ImageUploadConfig.WEBP_QUALITY,
        )

        val key = "organizations/$organizationId/stores/$storeId/products/${productId}_v1.webp"
        storage.from(Buckets.PRODUCT_IMAGES).upload(key, bytes) { upsert = true }
        key
    }
}
