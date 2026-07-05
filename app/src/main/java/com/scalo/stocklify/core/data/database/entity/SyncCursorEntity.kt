package com.scalo.stocklify.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "sync_cursor",
    primaryKeys = ["table_name", "store_id", "organization_id"]
)
data class SyncCursorEntity(

    @ColumnInfo(name = "table_name")
    val tableName: String,

    // Empty string sentinel for "not applicable" — Room/SQLite disallows nullable primary key columns.
    // Exactly one of storeId/organizationId is expected to be non-empty for a given cursor row.
    @ColumnInfo(name = "store_id")
    val storeId: String,

    @ColumnInfo(name = "organization_id")
    val organizationId: String,

    @ColumnInfo(name = "last_synced_version")
    val lastSyncedVersion: Long
)
