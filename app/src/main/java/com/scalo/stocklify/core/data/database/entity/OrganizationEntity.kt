package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.enums.SyncStatus

@Entity(tableName = "organizations")
data class OrganizationEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

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
