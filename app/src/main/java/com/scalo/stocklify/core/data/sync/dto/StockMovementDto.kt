package com.scalo.stocklify.core.data.sync.dto

import com.scalo.stocklify.core.model.enums.RecordStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockMovementDto(
    val id: String,
    @SerialName("store_product_id") val storeProductId: String,
    val quantity: Int,
    val reason: String,
    @SerialName("reference_id") val referenceId: String?,
    val notes: String?,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("created_by") val createdBy: String,
    val status: RecordStatus,
    val version: Long
)
