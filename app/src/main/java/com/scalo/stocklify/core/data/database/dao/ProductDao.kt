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
    suspend fun getById(id: Long): ProductEntity?

    @Query("""
        SELECT *
        FROM products
        WHERE code = :code
    """)
    suspend fun getByCode(code: String): ProductEntity?

    @Query("""
        SELECT *
        FROM products
        WHERE category_id = :categoryId
        ORDER BY name
    """)
    fun getByCategoryId(categoryId: Long): Flow<List<ProductEntity>>

    @Query("""
        SELECT *
        FROM products
        ORDER BY name
    """)
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int
}
