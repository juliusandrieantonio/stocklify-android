package com.scalo.stocklify.feature.products.model

import android.net.Uri
import com.scalo.stocklify.core.model.models.Category
import com.scalo.stocklify.core.model.models.Product

data class ScannedProductResult(
    val rawText: String,
    val productName: String?,
    val price: Double?,
    val stock: Long? = null,
    val imageUri: Uri? = null,
    val matchedCategory: Category?,
    val suggestedProduct: Product?,
    val similarityScore: Float = 0f
)
