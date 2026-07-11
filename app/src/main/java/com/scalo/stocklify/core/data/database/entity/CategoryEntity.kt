package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.util.constants.Tables

@Entity(tableName = Tables.CATEGORIES)
data class CategoryEntity(

    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "status")
    val status: RecordStatus,

    @ColumnInfo(name = "version")
    val version: Long
)
