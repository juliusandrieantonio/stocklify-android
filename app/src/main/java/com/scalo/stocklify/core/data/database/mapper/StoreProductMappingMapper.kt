package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.StoreProductMappingEntity
import com.scalo.stocklify.core.data.sync.dto.StoreProductMappingDto
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.model.models.StoreProductMapping

fun StoreProductMappingEntity.toDomain(): StoreProductMapping {
    return StoreProductMapping(
        id = id,
        storeId = storeId,
        productId = productId,
        categoryCode = categoryCode,
        supplierId = supplierId,
        productCode = productCode,
        barcode = barcode,
        costPrice = costPrice,
        sellingPrice = sellingPrice,
        quantity = quantity,
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

fun StoreProductMapping.toEntity(): StoreProductMappingEntity {
    return StoreProductMappingEntity(
        id = id,
        storeId = storeId,
        productId = productId,
        categoryCode = categoryCode,
        supplierId = supplierId,
        productCode = productCode,
        barcode = barcode,
        costPrice = costPrice,
        sellingPrice = sellingPrice,
        quantity = quantity,
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

fun StoreProductMapping.toDto() = StoreProductMappingDto(
    id = id,
    storeId = storeId,
    productId = productId,
    categoryCode = categoryCode,
    supplierId = supplierId,
    productCode = productCode,
    barcode = barcode,
    costPrice = costPrice,
    sellingPrice = sellingPrice,
    quantity = quantity,
    createdAt = createdAt,
    createdBy = createdBy ?: "",
    updatedAt = updatedAt,
    updatedBy = updatedBy,
    deletedAt = deletedAt,
    deletedBy = deletedBy,
    status = status,
    version = version
)

fun StoreProductMappingDto.toEntity() = StoreProductMappingEntity(
    id = id,
    storeId = storeId,
    productId = productId,
    categoryCode = categoryCode,
    supplierId = supplierId,
    productCode = productCode,
    barcode = barcode,
    costPrice = costPrice,
    sellingPrice = sellingPrice,
    quantity = quantity,
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