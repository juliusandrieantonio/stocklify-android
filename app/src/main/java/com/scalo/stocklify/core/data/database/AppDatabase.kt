package com.scalo.stocklify.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.entity.StoreEntity
import com.scalo.stocklify.core.data.database.converter.RecordStatusConverter

@Database(
    entities = [
        StoreEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecordStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storeDao(): StoreDao
}