package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.SyncCursorEntity

@Dao
interface SyncCursorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SyncCursorEntity)

    @Query("""
        SELECT last_synced_version
        FROM sync_cursor
        WHERE table_name = :tableName AND store_id = :storeId AND organization_id = :organizationId
    """)
    suspend fun getLastSyncedVersion(tableName: String, storeId: String, organizationId: String): Long?
}
