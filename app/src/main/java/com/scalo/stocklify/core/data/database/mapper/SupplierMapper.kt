package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.SupplierEntity
import com.scalo.stocklify.core.data.sync.dto.SupplierDto
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.model.models.Supplier

fun SupplierEntity.toDomain(): Supplier {
    return Supplier(
        id = id,
        storeId = storeId,
        name = name,
        contactPerson = contactPerson,
        phone = phone,
        email = email,
        address = address,
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

fun Supplier.toEntity(): SupplierEntity {
    return SupplierEntity(
        id = id,
        storeId = storeId,
        name = name,
        contactPerson = contactPerson,
        phone = phone,
        email = email,
        address = address,
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

fun SupplierDto.toEntity() = SupplierEntity(
    id = id,
    storeId = storeId,
    name = name,
    contactPerson = contactPerson,
    phone = phone,
    email = email,
    address = address,
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