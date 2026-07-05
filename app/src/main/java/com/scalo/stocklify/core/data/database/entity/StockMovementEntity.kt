package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.enums.SyncStatus

@Entity(
    tableName = "stock_movements",
    foreignKeys = [
        ForeignKey(
            entity = StoreProductMappingEntity::class,
            parentColumns = ["id"],
            childColumns = ["store_product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("store_product_id")
    ]
)
data class StockMovementEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "store_product_id")
    val storeProductId: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "reason")
    val reason: String,

    @ColumnInfo(name = "reference_id")
    val referenceId: String?,

    @ColumnInfo(name = "notes")
    val notes: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "created_by")
    val createdBy: String,

    @ColumnInfo(name = "status")
    val status: RecordStatus,

    @ColumnInfo(name = "version")
    val version: Long,

    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
