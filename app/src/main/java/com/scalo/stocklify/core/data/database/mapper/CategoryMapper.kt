package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.CategoryEntity
import com.scalo.stocklify.core.model.models.Category

fun CategoryEntity.toDomain(): Category {
    return Category(
        code = code,
        name = name,
        description = description,
        status = status,
        version = version
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        code = code,
        name = name,
        description = description,
        status = status,
        version = version
    )
}
