package com.scalo.stocklify.feature.salesmode.ui

data class ProductModel(
    val id: Long = 0,
    val code: String,
    val name: String,
    val sellingPrice: Double,
    val imageUrl: String? = null,
    val categoryId: Long,
    val stock: Long,
)
