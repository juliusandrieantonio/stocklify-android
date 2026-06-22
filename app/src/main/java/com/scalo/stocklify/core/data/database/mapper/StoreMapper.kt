package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.StoreEntity
import com.scalo.stocklify.core.model.models.Store

fun StoreEntity.toDomain(): Store {
    return Store(
        id = id,
        storeCode = storeCode,
        name = name,
        storeTypeId = storeTypeId,
        createdAt = createdAt,
        createdBy = createdBy,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}

fun Store.toEntity(): StoreEntity {
    return StoreEntity(
        id = id,
        storeCode = storeCode,
        name = name,
        storeTypeId = storeTypeId ?: 0,
        createdAt = createdAt,
        createdBy = createdBy ?: "",
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}