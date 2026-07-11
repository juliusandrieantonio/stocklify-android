package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.UserStoreMappingEntity
import com.scalo.stocklify.core.data.sync.dto.UserStoreMappingDto
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.model.models.UserStoreMapping

fun UserStoreMappingEntity.toDomain(): UserStoreMapping {
    return UserStoreMapping(
        id = id,
        userId = userId,
        storeId = storeId,
        isDefaultStore = isDefaultStore,
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

fun UserStoreMapping.toEntity(): UserStoreMappingEntity {
    return UserStoreMappingEntity(
        id = id,
        userId = userId,
        storeId = storeId,
        isDefaultStore = isDefaultStore,
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

fun UserStoreMapping.toDto() = UserStoreMappingDto(
    id = id,
    userId = userId,
    storeId = storeId,
    isDefaultStore = isDefaultStore,
    createdAt = createdAt,
    createdBy = createdBy ?: "",
    updatedAt = updatedAt,
    updatedBy = updatedBy,
    deletedAt = deletedAt,
    deletedBy = deletedBy,
    status = status,
    version = version
)

fun UserStoreMappingDto.toEntity() = UserStoreMappingEntity(
    id = id,
    userId = userId,
    storeId = storeId,
    isDefaultStore = isDefaultStore,
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
