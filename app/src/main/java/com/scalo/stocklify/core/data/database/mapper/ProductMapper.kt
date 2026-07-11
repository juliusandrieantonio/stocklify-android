package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.ProductEntity
import com.scalo.stocklify.core.data.sync.dto.ProductDto
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.model.models.Product

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        organizationId = organizationId,
        name = name,
        description = description,
        imageUrl = imageUrl,
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

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        organizationId = organizationId,
        name = name,
        description = description,
        imageUrl = imageUrl,
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

fun Product.toDto() = ProductDto(
    id = id,
    organizationId = organizationId ?: "",
    name = name,
    description = description,
    imageUrl = imageUrl,
    createdAt = createdAt,
    createdBy = createdBy ?: "",
    updatedAt = updatedAt,
    updatedBy = updatedBy,
    deletedAt = deletedAt,
    deletedBy = deletedBy,
    status = status,
    version = version
)

fun ProductDto.toEntity() = ProductEntity(
    id = id,
    organizationId = organizationId,
    name = name,
    description = description,
    imageUrl = imageUrl,
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