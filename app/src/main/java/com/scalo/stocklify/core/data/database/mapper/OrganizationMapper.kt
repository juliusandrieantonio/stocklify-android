package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.OrganizationEntity
import com.scalo.stocklify.core.data.sync.dto.OrganizationDto
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.model.models.Organization

fun OrganizationEntity.toDomain(): Organization {
    return Organization(
        id = id,
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

fun Organization.toEntity(): OrganizationEntity {
    return OrganizationEntity(
        id = id,
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

fun Organization.toDto() = OrganizationDto(
    id = id,
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

fun OrganizationDto.toEntity() = OrganizationEntity(
    id = id,
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