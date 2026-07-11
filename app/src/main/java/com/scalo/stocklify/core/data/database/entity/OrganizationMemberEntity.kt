package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.util.constants.Tables

/**
 * No FK to OrganizationEntity(id) on purpose: the ORGANIZATION pull phase fetches
 * memberships before organizations (it needs membership rows to know which org ids
 * to fetch), so a member row can legitimately land locally before its parent org row.
 */
@Entity(
    tableName = Tables.ORGANIZATION_MEMBERS,
    indices = [
        Index("organization_id"),
        Index("user_id")
    ]
)
data class OrganizationMemberEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "organization_id")
    val organizationId: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "role")
    val role: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "created_by")
    val createdBy: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long?,

    @ColumnInfo(name = "updated_by")
    val updatedBy: String?,

    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long?,

    @ColumnInfo(name = "deleted_by")
    val deletedBy: String?,

    @ColumnInfo(name = "status")
    val status: RecordStatus,

    @ColumnInfo(name = "version")
    val version: Long,

    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
