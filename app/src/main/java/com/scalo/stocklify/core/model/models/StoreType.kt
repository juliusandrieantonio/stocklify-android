package com.scalo.stocklify.core.model.models

import com.scalo.stocklify.core.model.enums.RecordStatus

data class StoreType(
    val id: Long = 0,
    val code: String,
    val name: String,

    val createdAt: Long = 0,
    val createdBy: String? = null,

    val updatedAt: Long? = null,
    val updatedBy: String? = null,

    val status: RecordStatus = RecordStatus.ACTIVE,
    val version: Long = 0
)
