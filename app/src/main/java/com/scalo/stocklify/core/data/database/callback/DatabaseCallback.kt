package com.scalo.stocklify.core.data.database.callback

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.scalo.stocklify.core.data.database.migration.STORE_TYPE_SEED_SQL

class DatabaseCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        STORE_TYPE_SEED_SQL.forEach { db.execSQL(it) }
    }
}
