package com.scalo.stocklify.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.scalo.stocklify.core.data.database.entity.StoreProductMappingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreProductMappingDao : BaseDao<StoreProductMappingEntity> {

    @Query("""
        SELECT *
        FROM store_product_mapping
        WHERE id = :id
    """)
    suspend fun getById(id: Long): StoreProductMappingEntity?

    @Query("""
        SELECT *
        FROM store_product_mapping
        WHERE store_id = :storeId
        ORDER BY id
    """)
    fun getByStoreId(storeId: Long): Flow<List<StoreProductMappingEntity>>

    @Query("""
        SELECT *
        FROM store_product_mapping
        WHERE store_id = :storeId AND product_id = :productId
    """)
    suspend fun getByStoreAndProduct(storeId: Long, productId: Long): StoreProductMappingEntity?

    @Query("""
        SELECT *
        FROM store_product_mapping
        WHERE store_id = :storeId AND supplier_id = :supplierId
        ORDER BY id
    """)
    fun getByStoreAndSupplier(storeId: Long, supplierId: Long): Flow<List<StoreProductMappingEntity>>

    @Query("SELECT COUNT(*) FROM store_product_mapping WHERE store_id = :storeId")
    suspend fun getCountByStoreId(storeId: Long): Int
}
