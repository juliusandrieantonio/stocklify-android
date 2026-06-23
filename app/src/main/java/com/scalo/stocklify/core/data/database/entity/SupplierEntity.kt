package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.RecordStatus

@Entity(
    tableName = "suppliers",
    indices = [
        Index(value = ["store_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = ["id"],
            childColumns = ["store_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class SupplierEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "store_id")
    val storeId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "contact_person")
    val contactPerson: String?,

    @ColumnInfo(name = "phone")
    val phone: String?,

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
