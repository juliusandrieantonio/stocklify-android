package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scalo.stocklify.core.model.enums.SyncStatus
import com.scalo.stocklify.core.util.constants.Tables

@Entity(tableName = Tables.SYNC_OUTBOX)
data class SyncOutboxEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "table_name")
    val tableName: String,

    @ColumnInfo(name = "operation")
    val operation: String,

    @ColumnInfo(name = "payload_json")
    val payloadJson: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "last_error")
    val lastError: Int = 0,

    @ColumnInfo(name = "retry_count")
    val retryCount: Int = 0,

    @ColumnInfo(name = "status")
    val status: SyncStatus
)
