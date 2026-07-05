package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import com.scalo.stocklify.core.data.database.entity.SyncOutboxEntity

@Dao
interface SyncOutboxDao : BaseDao<SyncOutboxEntity>
