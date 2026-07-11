package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.util.constants.Tables

@Entity(
    tableName = Tables.STORES,
    foreignKeys = [
        ForeignKey(
            entity = OrganizationEntity::class,
            parentColumns = ["id"],
            childColumns = ["organization_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StoreTypeEntity::class,
            parentColumns = ["code"],
            childColumns = ["store_type_code"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("organization_id"),
        Index("store_type_code"),
        Index(value = ["store_code"], unique = true)
    ]
)
data class StoreEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "organization_id")
    val organizationId: String? = null,

    @ColumnInfo(name = "store_code")
    val storeCode: String,

    @ColumnInfo(name = "store_type_code")
    val storeTypeCode: String,

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
