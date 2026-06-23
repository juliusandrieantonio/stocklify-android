package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.SupplierEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao : BaseDao<SupplierEntity> {

    @Query("""
        SELECT *
        FROM suppliers
        WHERE id = :id
    """)
    suspend fun getById(id: Long): SupplierEntity?

    @Query("""
        SELECT *
        FROM suppliers
        WHERE store_id = :storeId
        ORDER BY name
    """)
    fun getByStoreId(storeId: Long): Flow<List<SupplierEntity>>

    @Query("""
        SELECT *
        FROM suppliers
        ORDER BY name
    """)
    fun getAll(): Flow<List<SupplierEntity>>

    @Query("SELECT COUNT(*) FROM suppliers WHERE store_id = :storeId")
    suspend fun getCountByStoreId(storeId: Long): Int
}
