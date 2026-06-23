package com.scalo.stocklify.core.model.models

import com.scalo.stocklify.core.model.enums.RecordStatus

data class Product(
    val id: Long = 0,
    val code: String,
    val name: String,
    val sellingPrice: Double,
    val costPrice: Double,
    val imageUrl: String? = null,
    val categoryId: Long,

    val createdAt: Long = 0,
    val createdBy: String? = null,

    val updatedAt: Long? = null,
    val updatedBy: String? = null,

    val status: RecordStatus = RecordStatus.ACTIVE,
    val version: Long = 0
)
