package com.scalo.stocklify.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scalo.stocklify.core.data.database.converter.RecordStatusConverter
import com.scalo.stocklify.core.data.database.converter.SyncStatusConverter
import com.scalo.stocklify.core.data.database.dao.CategoryDao
import com.scalo.stocklify.core.data.database.dao.OrganizationDao
import com.scalo.stocklify.core.data.database.dao.OrganizationMemberDao
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.dao.StockMovementDao
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.dao.StoreTypeDao
import com.scalo.stocklify.core.data.database.dao.SupplierDao
import com.scalo.stocklify.core.data.database.dao.SyncCursorDao
import com.scalo.stocklify.core.data.database.dao.SyncOutboxDao
import com.scalo.stocklify.core.data.database.dao.UserStoreMappingDao
import com.scalo.stocklify.core.data.database.entity.CategoryEntity
import com.scalo.stocklify.core.data.database.entity.OrganizationEntity
import com.scalo.stocklify.core.data.database.entity.OrganizationMemberEntity
import com.scalo.stocklify.core.data.database.entity.ProductEntity
import com.scalo.stocklify.core.data.database.entity.StockMovementEntity
import com.scalo.stocklify.core.data.database.entity.StoreEntity
import com.scalo.stocklify.core.data.database.entity.StoreProductMappingEntity
import com.scalo.stocklify.core.data.database.entity.StoreTypeEntity
import com.scalo.stocklify.core.data.database.entity.SupplierEntity
import com.scalo.stocklify.core.data.database.entity.SyncCursorEntity
import com.scalo.stocklify.core.data.database.entity.SyncOutboxEntity
import com.scalo.stocklify.core.data.database.entity.UserStoreMappingEntity

@Database(
    entities = [
        StoreTypeEntity::class,
        CategoryEntity::class,
        OrganizationEntity::class,
        OrganizationMemberEntity::class,
        StoreEntity::class,
        UserStoreMappingEntity::class,
        SupplierEntity::class,
        ProductEntity::class,
        StoreProductMappingEntity::class,
        StockMovementEntity::class,
        SyncCursorEntity::class,
        SyncOutboxEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecordStatusConverter::class, SyncStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storeTypeDao(): StoreTypeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun organizationDao(): OrganizationDao
    abstract fun organizationMemberDao(): OrganizationMemberDao
    abstract fun storeDao(): StoreDao
    abstract fun userStoreMappingDao(): UserStoreMappingDao
    abstract fun supplierDao(): SupplierDao
    abstract fun productDao(): ProductDao
    abstract fun storeProductMappingDao(): StoreProductMappingDao
    abstract fun stockMovementDao(): StockMovementDao
    abstract fun syncCursorDao(): SyncCursorDao
    abstract fun syncOutboxDao(): SyncOutboxDao
}
