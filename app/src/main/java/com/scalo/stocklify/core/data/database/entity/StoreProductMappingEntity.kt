package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.enums.SyncStatus

@Entity(
    tableName = "store_product_mapping",
    foreignKeys = [
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = ["id"],
            childColumns = ["store_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SupplierEntity::class,
            parentColumns = ["id"],
            childColumns = ["supplier_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["code"],
            childColumns = ["category_code"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("store_id"),
        Index("product_id"),
        Index("supplier_id"),
        Index("category_code"),
        Index(value = ["store_id", "product_id"], unique = true)
    ]
)
data class StoreProductMappingEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "store_id")
    val storeId: String,

    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "category_code")
    val categoryCode: String,

    @ColumnInfo(name = "supplier_id")
    val supplierId: String?,

    @ColumnInfo(name = "product_code")
    val productCode: String?,

    @ColumnInfo(name = "barcode")
    val barcode: String?,

    @ColumnInfo(name = "cost_price")
    val costPrice: Double,

    @ColumnInfo(name = "selling_price")
    val sellingPrice: Double,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

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
