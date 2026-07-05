package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.StoreTypeEntity
import com.scalo.stocklify.core.model.models.StoreType

fun StoreTypeEntity.toDomain(): StoreType {
    return StoreType(
        code = code,
        name = name,
        status = status,
        version = version
    )
}

fun StoreType.toEntity(): StoreTypeEntity {
    return StoreTypeEntity(
        code = code,
        name = name,
        status = status,
        version = version
    )
}
