package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.StockMovementEntity
import com.scalo.stocklify.core.data.sync.dto.StockMovementDto
import com.scalo.stocklify.core.model.enums.StockMovementReason
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.model.models.StockMovement

fun StockMovementEntity.toDomain(): StockMovement {
    return StockMovement(
        id = id,
        storeProductId = storeProductId,
        quantity = quantity,
        reason = StockMovementReason.from(reason),
        referenceId = referenceId,
        notes = notes,
        createdAt = createdAt,
        createdBy = createdBy,
        status = status,
        version = version,
        syncStatus = syncStatus
    )
}

fun StockMovement.toEntity(): StockMovementEntity {
    return StockMovementEntity(
        id = id,
        storeProductId = storeProductId,
        quantity = quantity,
        reason = reason.value,
        referenceId = referenceId,
        notes = notes,
        createdAt = createdAt,
        createdBy = createdBy ?: "",
        status = status,
        version = version,
        syncStatus = syncStatus
    )
}

fun StockMovement.toDto() = StockMovementDto(
    id = id,
    storeProductId = storeProductId,
    quantity = quantity,
    reason = reason.value,
    referenceId = referenceId,
    notes = notes,
    createdAt = createdAt,
    createdBy = createdBy ?: "",
    status = status,
    version = version
)

fun StockMovementDto.toEntity() = StockMovementEntity(
    id = id,
    storeProductId = storeProductId,
    quantity = quantity,
    reason = reason,
    referenceId = referenceId,
    notes = notes,
    createdAt = createdAt,
    createdBy = createdBy,
    status = status,
    version = version,
    syncStatus = SyncStatus.SYNCED
)