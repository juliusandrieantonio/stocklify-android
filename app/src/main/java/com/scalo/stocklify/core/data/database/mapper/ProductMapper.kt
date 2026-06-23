package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.ProductEntity
import com.scalo.stocklify.core.model.models.Product

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        code = code,
        name = name,
        sellingPrice = sellingPrice,
        costPrice = costPrice,
        imageUrl = imageUrl,
        categoryId = categoryId,
        createdAt = createdAt,
        createdBy = createdBy,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        code = code,
        name = name,
        sellingPrice = sellingPrice,
        costPrice = costPrice,
        imageUrl = imageUrl,
        categoryId = categoryId,
        createdAt = createdAt,
        createdBy = createdBy ?: "",
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        status = status,
        version = version
    )
}
