package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.UserEntity
import com.scalo.stocklify.core.data.sync.dto.UserDto
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.model.models.User

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        fullName = fullName,
        phone = phone,
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

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        fullName = fullName,
        phone = phone,
        createdAt = createdAt,
        createdBy = createdBy,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        deletedAt = deletedAt,
        deletedBy = deletedBy,
        status = status,
        version = version,
        syncStatus = syncStatus
    )
}

fun User.toDto() = UserDto(
    id = id,
    fullName = fullName,
    phone = phone,
    createdAt = createdAt,
    createdBy = createdBy,
    updatedAt = updatedAt,
    updatedBy = updatedBy,
    deletedAt = deletedAt,
    deletedBy = deletedBy,
    status = status,
    version = version
)

fun UserDto.toEntity() = UserEntity(
    id = id,
    fullName = fullName,
    phone = phone,
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
