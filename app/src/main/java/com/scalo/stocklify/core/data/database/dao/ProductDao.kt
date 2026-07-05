package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao : BaseDao<ProductEntity> {

    @Query("""
        SELECT *
        FROM products
        WHERE id = :id
    """)
    suspend fun getById(id: String): ProductEntity?

    @Query("""
        SELECT *
        FROM products
        WHERE organization_id = :organizationId
        ORDER BY name
    """)
    fun getByOrganizationId(organizationId: String): Flow<List<ProductEntity>>

    @Query("""
        SELECT *
        FROM products
        ORDER BY name
    """)
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int
}
