package com.scalo.stocklify.core.data.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface DevToolsDao {
    @RawQuery
    fun rawQuery(query: SupportSQLiteQuery): Cursor
}
