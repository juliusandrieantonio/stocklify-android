package com.scalo.stocklify.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scalo.stocklify.core.data.database.converter.RecordStatusConverter
import com.scalo.stocklify.core.data.database.dao.CategoryDao
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.dao.StoreTypeDao
import com.scalo.stocklify.core.data.database.dao.SupplierDao
import com.scalo.stocklify.core.data.database.entity.CategoryEntity
import com.scalo.stocklify.core.data.database.entity.ProductEntity
import com.scalo.stocklify.core.data.database.entity.StoreEntity
import com.scalo.stocklify.core.data.database.entity.StoreProductMappingEntity
import com.scalo.stocklify.core.data.database.entity.StoreTypeEntity
import com.scalo.stocklify.core.data.database.entity.SupplierEntity

@Database(
    entities = [
        StoreEntity::class,
        StoreTypeEntity::class,
        CategoryEntity::class,
        SupplierEntity::class,
        ProductEntity::class,
        StoreProductMappingEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(RecordStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storeDao(): StoreDao
    abstract fun storeTypeDao(): StoreTypeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun supplierDao(): SupplierDao
    abstract fun productDao(): ProductDao
    abstract fun storeProductMappingDao(): StoreProductMappingDao
}