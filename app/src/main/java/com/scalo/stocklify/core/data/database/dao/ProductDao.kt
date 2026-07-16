package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.ProductEntity
import com.scalo.stocklify.core.model.enums.RecordStatus
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
        WHERE status = 'ACTIVE'
        ORDER BY name
    """)
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int

    @Query("UPDATE products SET image_url = :imageUrl WHERE id = :id")
    suspend fun updateImageUrl(id: String, imageUrl: String)

    @Query("UPDATE products SET local_image_path = :localPath, cached_image_key = :cachedKey WHERE id = :id")
    suspend fun updateLocalImageCache(id: String, localPath: String?, cachedKey: String?)
}
