package com.scalo.stocklify.di

import android.content.Context
import androidx.room.Room
import com.scalo.stocklify.core.config.DatabaseConfig
import com.scalo.stocklify.core.data.database.AppDatabase
import com.scalo.stocklify.core.data.database.callback.DatabaseCallback
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
import com.scalo.stocklify.core.data.database.util.DatabaseKeyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        keyManager: DatabaseKeyManager
    ): AppDatabase {

        val passphrase = SQLiteDatabase.getBytes(
            runBlocking { keyManager.getOrCreateKey() }.toCharArray()
        )

        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DatabaseConfig.databaseName()
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration(true)
            .addCallback(DatabaseCallback())
            .build()
    }

    @Provides
    fun provideStoreTypeDao(database: AppDatabase): StoreTypeDao {
        return database.storeTypeDao()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideOrganizationDao(database: AppDatabase): OrganizationDao {
        return database.organizationDao()
    }

    @Provides
    fun provideOrganizationMemberDao(database: AppDatabase): OrganizationMemberDao {
        return database.organizationMemberDao()
    }

    @Provides
    fun provideStoreDao(database: AppDatabase): StoreDao {
        return database.storeDao()
    }

    @Provides
    fun provideUserStoreMappingDao(database: AppDatabase): UserStoreMappingDao {
        return database.userStoreMappingDao()
    }

    @Provides
    fun provideSupplierDao(database: AppDatabase): SupplierDao {
        return database.supplierDao()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideStoreProductMappingDao(database: AppDatabase): StoreProductMappingDao {
        return database.storeProductMappingDao()
    }

    @Provides
    fun provideStockMovementDao(database: AppDatabase): StockMovementDao {
        return database.stockMovementDao()
    }

    @Provides
    fun provideSyncCursorDao(database: AppDatabase): SyncCursorDao {
        return database.syncCursorDao()
    }

    @Provides
    fun provideSyncOutboxDao(database: AppDatabase): SyncOutboxDao {
        return database.syncOutboxDao()
    }
}
