package com.scalo.stocklify.core.model.models

import com.scalo.stocklify.core.model.enums.RecordStatus

data class StoreProductMapping(
    val id: Long = 0,
    val storeId: Long,
    val productId: Long,
    val quantity: Long,
    val supplierId: Long? = null,

    val createdAt: Long = 0,
    val createdBy: String? = null,

    val updatedAt: Long? = null,
    val updatedBy: String? = null,

    val status: RecordStatus = RecordStatus.ACTIVE,
    val version: Long = 0
)
