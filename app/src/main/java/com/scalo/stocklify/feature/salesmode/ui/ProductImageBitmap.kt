package com.scalo.stocklify.feature.salesmode.ui

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Resolves a product's image (local draft URI, or a Storage key that's
 * downloaded and cached on first access) to a displayable [ImageBitmap] via
 * [resolveImageFile], e.g. [SalesModeViewModel.resolveProductImageFile].
 */
@Composable
fun rememberProductImageBitmap(
    productId: String,
    imageUrl: String?,
    resolveImageFile: suspend (String, String?) -> File?,
): State<ImageBitmap?> {
    return produceState<ImageBitmap?>(initialValue = null, key1 = productId, key2 = imageUrl) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                resolveImageFile(productId, imageUrl)?.let { file ->
                    BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
                }
            }.getOrNull()
        }
    }
}
