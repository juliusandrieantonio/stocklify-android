package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.StockMovementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockMovementDao : BaseDao<StockMovementEntity> {

    @Query("""
        SELECT *
        FROM stock_movements
        WHERE store_product_id = :storeProductId
        ORDER BY created_at DESC
    """)
    fun getByStoreProductId(storeProductId: String): Flow<List<StockMovementEntity>>
}
