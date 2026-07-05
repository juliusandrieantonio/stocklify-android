package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao : BaseDao<CategoryEntity> {

    @Query("""
        SELECT *
        FROM categories
        WHERE code = :code
    """)
    suspend fun getByCode(code: String): CategoryEntity?

    @Query("""
        SELECT *
        FROM categories
        ORDER BY name
    """)
    fun getAll(): Flow<List<CategoryEntity>>

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCount(): Int
}
