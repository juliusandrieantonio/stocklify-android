package com.scalo.stocklify.core.data.sync.dto

import com.scalo.stocklify.core.model.enums.RecordStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserStoreMappingDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("store_id") val storeId: String,
    @SerialName("is_default_store") val isDefaultStore: Boolean,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("created_by") val createdBy: String,
    @SerialName("updated_at") val updatedAt: Long?,
    @SerialName("updated_by") val updatedBy: String?,
    @SerialName("deleted_at") val deletedAt: Long?,
    @SerialName("deleted_by") val deletedBy: String?,
    val status: RecordStatus,
    val version: Long
)
