package com.scalo.stocklify.core.data.database.converter

import androidx.room.TypeConverter
import com.scalo.stocklify.core.model.enums.RecordStatus

class RecordStatusConverter {

    @TypeConverter
    fun fromStatus(status: RecordStatus): String {
        return status.value
    }

    @TypeConverter
    fun toStatus(value: String): RecordStatus {
        return RecordStatus.from(value)
    }
}
