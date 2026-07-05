package com.scalo.stocklify.core.model.models

import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.enums.StockMovementReason
import com.scalo.stocklify.core.model.enums.SyncStatus
import java.util.UUID

data class StockMovement(
    val id: String = UUID.randomUUID().toString(),
    val storeProductId: String,
    val quantity: Int,
    val reason: StockMovementReason,
    val referenceId: String? = null,
    val notes: String? = null,

    val createdAt: Long = 0,
    val createdBy: String? = null,

    val status: RecordStatus = RecordStatus.ACTIVE,
    val version: Long = 0,

    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
