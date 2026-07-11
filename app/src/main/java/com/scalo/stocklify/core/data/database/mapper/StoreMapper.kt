package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.StoreEntity
import com.scalo.stocklify.core.data.sync.dto.StoreDto
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.model.models.Store

fun StoreEntity.toDomain(): Store {
    return Store(
        id = id,
        organizationId = organizationId,
        storeCode = storeCode,
        storeTypeCode = storeTypeCode,
        name = name,
        createdAt = createdAt,
        createdBy = createdBy,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version,
        deletedAt = deletedAt,
        deletedBy = deletedBy,
        syncStatus = syncStatus
    )
}

fun Store.toEntity(): StoreEntity {
    return StoreEntity(
        id = id,
        organizationId = organizationId,
        storeCode = storeCode,
        storeTypeCode = storeTypeCode,
        name = name,
        createdAt = createdAt,
        createdBy = createdBy ?: "",
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        deletedAt = deletedAt,
        deletedBy = deletedBy,
        status = status,
        version = version,
        syncStatus = syncStatus
    )
}


fun Store.toDto() = StoreDto(
    id = id,
    organizationId = organizationId ?: "",
    storeCode = storeCode,
    storeTypeCode = storeTypeCode,
    name = name,
    createdAt = createdAt,
    createdBy = createdBy ?: "",
    updatedAt = updatedAt,
    updatedBy = updatedBy,
    deletedAt = deletedAt,
    deletedBy = deletedBy,
    status = status,
    version = version
)

fun StoreDto.toEntity() = StoreEntity(
    id = id,
    organizationId = organizationId,
    storeCode = storeCode,
    storeTypeCode = storeTypeCode,
    name = name,
    createdAt = createdAt,
    createdBy = createdBy,
    updatedAt = updatedAt,
    updatedBy = updatedBy,
    deletedAt = deletedAt,
    deletedBy = deletedBy,
    status = status,
    version = version,
    syncStatus = SyncStatus.SYNCED
)