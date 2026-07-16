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
    suspend fun getById(id: String): StoreEntity?

    @Query("""
        SELECT *
        FROM stores
        WHERE store_code = :storeCode
    """)
    suspend fun getByStoreCode(storeCode: String): StoreEntity?

    @Query("""
        SELECT *
        FROM stores
        WHERE status = 'ACTIVE'
        ORDER BY name
    """)
    fun getAll(): Flow<List<StoreEntity>>

    @Query("SELECT COUNT(*) FROM stores")
    suspend fun getStoreCount(): Int

    @Query("""
        SELECT id
        FROM stores
        WHERE organization_id IN (:organizationIds)
    """)
    suspend fun getIdsByOrganizationIds(organizationIds: List<String>): List<String>
}