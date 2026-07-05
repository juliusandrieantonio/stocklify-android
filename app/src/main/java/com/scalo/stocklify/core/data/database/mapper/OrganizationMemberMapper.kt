package com.scalo.stocklify.core.data.database.mapper

import com.scalo.stocklify.core.data.database.entity.OrganizationMemberEntity
import com.scalo.stocklify.core.model.enums.MembershipRole
import com.scalo.stocklify.core.model.models.OrganizationMember

fun OrganizationMemberEntity.toDomain(): OrganizationMember {
    return OrganizationMember(
        id = id,
        organizationId = organizationId,
        userId = userId,
        role = MembershipRole.from(role),
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

fun OrganizationMember.toEntity(): OrganizationMemberEntity {
    return OrganizationMemberEntity(
        id = id,
        organizationId = organizationId,
        userId = userId,
        role = role.value,
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
