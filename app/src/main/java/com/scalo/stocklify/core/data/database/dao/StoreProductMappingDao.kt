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
    suspend fun getById(id: String): StoreProductMappingEntity?

    @Query("""
        SELECT *
        FROM store_product_mapping
        WHERE store_id = :storeId
        ORDER BY id
    """)
    fun getByStoreId(storeId: String): Flow<List<StoreProductMappingEntity>>

    @Query("""
        SELECT *
        FROM store_product_mapping
        WHERE store_id = :storeId AND product_id = :productId
    """)
    suspend fun getByStoreAndProduct(storeId: String, productId: String): StoreProductMappingEntity?

    @Query("""
        SELECT *
        FROM store_product_mapping
        WHERE store_id = :storeId AND supplier_id = :supplierId
        ORDER BY id
    """)
    fun getByStoreAndSupplier(storeId: String, supplierId: String): Flow<List<StoreProductMappingEntity>>

    @Query("SELECT COUNT(*) FROM store_product_mapping WHERE store_id = :storeId")
    suspend fun getCountByStoreId(storeId: String): Int
}
