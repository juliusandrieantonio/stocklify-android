package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.StoreTypeEntity
import com.scalo.stocklify.core.model.models.StoreType

fun StoreTypeEntity.toDomain(): StoreType {
    return StoreType(
        id = id,
        code = code,
        name = name,
        createdAt = createdAt,
        createdBy = createdBy,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}

fun StoreType.toEntity(): StoreTypeEntity {
    return StoreTypeEntity(
        id = id,
        code = code,
        name = name,
        createdAt = createdAt,
        createdBy = createdBy ?: "",
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}
