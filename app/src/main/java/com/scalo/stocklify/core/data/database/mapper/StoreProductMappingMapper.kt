package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.StoreProductMappingEntity
import com.scalo.stocklify.core.model.models.StoreProductMapping

fun StoreProductMappingEntity.toDomain(): StoreProductMapping {
    return StoreProductMapping(
        id = id,
        storeId = storeId,
        productId = productId,
        quantity = quantity,
        supplierId = supplierId,
        createdAt = createdAt,
        createdBy = createdBy,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}

fun StoreProductMapping.toEntity(): StoreProductMappingEntity {
    return StoreProductMappingEntity(
        id = id,
        storeId = storeId,
        productId = productId,
        quantity = quantity,
        supplierId = supplierId,
        createdAt = createdAt,
        createdBy = createdBy ?: "",
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}
