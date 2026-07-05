package com.scalo.stocklify.feature.salesmode.ui

data class ProductModel(
    val id: String,
    val storeProductId: String,
    val code: String,
    val name: String,
    val sellingPrice: Double,
    val imageUrl: String? = null,
    val categoryCode: String,
    val stock: Int,
)
