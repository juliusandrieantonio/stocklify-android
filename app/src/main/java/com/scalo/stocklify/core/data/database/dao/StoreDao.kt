package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao : BaseDao<StoreEntity> {

    @Query("""
        SELECT *
        FROM stores
        WHERE id = :id
    """)
    suspend fun getById(id: Long): StoreEntity?

    @Query("""
        SELECT *
        FROM stores
        WHERE store_code = :storeCode
    """)
    suspend fun getByStoreCode(storeCode: String): StoreEntity?

    @Query("""
        SELECT *
        FROM stores
        ORDER BY name
    """)
    fun getAll(): Flow<List<StoreEntity>>

    @Query("SELECT COUNT(*) FROM stores")
    suspend fun getStoreCount(): Int
}