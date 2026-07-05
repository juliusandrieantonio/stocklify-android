package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.scalo.stocklify.core.data.database.entity.SyncCursorEntity

@Dao
interface SyncCursorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SyncCursorEntity)
}
