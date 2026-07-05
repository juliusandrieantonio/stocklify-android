package com.scalo.stocklify.core.data.database.converter

import androidx.room.TypeConverter
import com.scalo.stocklify.core.model.enums.SyncStatus

class SyncStatusConverter {

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String {
        return status.value
    }

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus {
        return SyncStatus.from(value)
    }
}
