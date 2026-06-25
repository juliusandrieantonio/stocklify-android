package com.scalo.stocklify.feature.products.model

data class ProductExtraction(
    val name: String?,
    val price: Double?,
    val quantityValue: Double?,
    val quantityUnit: String?,
    val confidence: Float
)
