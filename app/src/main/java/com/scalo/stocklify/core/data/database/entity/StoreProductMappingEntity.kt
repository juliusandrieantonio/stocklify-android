package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.RecordStatus

@Entity(
    tableName = "store_product_mapping",
    indices = [
        Index(value = ["store_id"]),
        Index(value = ["product_id"]),
        Index(value = ["supplier_id"]),
        Index(value = ["store_id", "product_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = ["id"],
            childColumns = ["store_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SupplierEntity::class,
            parentColumns = ["id"],
            childColumns = ["supplier_id"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class StoreProductMappingEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "store_id")
    val storeId: Long,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "quantity")
    val quantity: Long,

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long?,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long?,

    @ColumnInfo(name = "updated_by")
    val updatedBy: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "created_by")
    val createdBy: String,

    @ColumnInfo(name = "status")
    val status: RecordStatus,

    @ColumnInfo(name = "version")
    val version: Long
)
