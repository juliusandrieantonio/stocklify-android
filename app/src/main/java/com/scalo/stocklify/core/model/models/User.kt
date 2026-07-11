package com.scalo.stocklify.core.model.models

import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.enums.SyncStatus

data class User(
    val id: String,
    val fullName: String? = null,
    val phone: String? = null,

    val createdAt: Long = 0,
    val createdBy: String? = null,

    val updatedAt: Long? = null,
    val updatedBy: String? = null,

    val status: RecordStatus = RecordStatus.ACTIVE,
    val version: Long = 1,

    val deletedAt: Long? = null,
    val deletedBy: String? = null,

    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
