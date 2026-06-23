package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.SupplierEntity
import com.scalo.stocklify.core.model.models.Supplier

fun SupplierEntity.toDomain(): Supplier {
    return Supplier(
        id = id,
        storeId = storeId,
        name = name,
        contactPerson = contactPerson,
        phone = phone,
        createdAt = createdAt,
        createdBy = createdBy,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}

fun Supplier.toEntity(): SupplierEntity {
    return SupplierEntity(
        id = id,
        storeId = storeId,
        name = name,
        contactPerson = contactPerson,
        phone = phone,
        createdAt = createdAt,
        createdBy = createdBy ?: "",
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}
