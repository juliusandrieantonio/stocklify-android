package com.scalo.stocklify.feature.products.model

import com.scalo.stocklify.core.model.models.Category
import com.scalo.stocklify.core.model.models.Product

data class ScannedProductResult(
    val rawText: String,
    val productName: String?,
    val price: Double?,
    val matchedCategory: Category?,
    val suggestedProduct: Product?,
    val similarityScore: Float = 0f
)
