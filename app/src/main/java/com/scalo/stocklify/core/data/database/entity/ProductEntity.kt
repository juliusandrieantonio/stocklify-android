package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.RecordStatus

@Entity(
    tableName = "products",
    indices = [
        Index(value = ["code"], unique = true),
        Index(value = ["category_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ProductEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "code")
    val code: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "selling_price")
    val sellingPrice: Double,

    @ColumnInfo(name = "cost_price")
    val costPrice: Double,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?,

    @ColumnInfo(name = "category_id")
    val categoryId: Long,

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
