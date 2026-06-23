package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.StoreTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreTypeDao : BaseDao<StoreTypeEntity> {

    @Query("""
        SELECT *
        FROM store_types
        WHERE id = :id
    """)
    suspend fun getById(id: Long): StoreTypeEntity?

    @Query("""
        SELECT *
        FROM store_types
        WHERE code = :code
    """)
    suspend fun getByCode(code: String): StoreTypeEntity?

    @Query("""
        SELECT *
        FROM store_types
        ORDER BY name
    """)
    fun getAll(): Flow<List<StoreTypeEntity>>

    @Query("SELECT COUNT(*) FROM store_types")
    suspend fun getCount(): Int
}
