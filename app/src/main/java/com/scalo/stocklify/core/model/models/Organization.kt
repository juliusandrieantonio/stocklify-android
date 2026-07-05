package com.scalo.stocklify.core.model.models

import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.enums.SyncStatus
import java.util.UUID

data class Organization(
    val id: String = UUID.randomUUID().toString(),
    val name: String,

    val createdAt: Long = 0,
    val createdBy: String? = null,

    val updatedAt: Long? = null,
    val updatedBy: String? = null,

    val status: RecordStatus = RecordStatus.ACTIVE,
    val version: Long = 0,

    val deletedAt: Long? = null,
    val deletedBy: String? = null,

    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
