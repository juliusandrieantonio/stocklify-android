package com.scalo.stocklify.core.data.sync.dto

import com.scalo.stocklify.core.model.enums.RecordStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreProductMappingDto(
    val id: String,
    @SerialName("store_id") val storeId: String,
    @SerialName("product_id") val productId: String,
    @SerialName("category_code") val categoryCode: String,
    @SerialName("supplier_id") val supplierId: String?,
    @SerialName("product_code") val productCode: String?,
    val barcode: String?,
    @SerialName("cost_price") val costPrice: Double,
    @SerialName("selling_price") val sellingPrice: Double,
    val quantity: Int,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("created_by") val createdBy: String,
    @SerialName("updated_at") val updatedAt: Long?,
    @SerialName("updated_by") val updatedBy: String?,
    @SerialName("deleted_at") val deletedAt: Long?,
    @SerialName("deleted_by") val deletedBy: String?,
    val status: RecordStatus,
    val version: Long
)
