package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.UserStoreMappingEntity
import com.scalo.stocklify.core.model.enums.MembershipRole
import com.scalo.stocklify.core.model.models.UserStoreMapping

fun UserStoreMappingEntity.toDomain(): UserStoreMapping {
    return UserStoreMapping(
        id = id,
        userId = userId,
        storeId = storeId,
        role = MembershipRole.from(role),
        isDefaultStore = isDefaultStore,
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

fun UserStoreMapping.toEntity(): UserStoreMappingEntity {
    return UserStoreMappingEntity(
        id = id,
        userId = userId,
        storeId = storeId,
        role = role.value,
        isDefaultStore = isDefaultStore,
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
