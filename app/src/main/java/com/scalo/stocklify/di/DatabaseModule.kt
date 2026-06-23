package com.scalo.stocklify.di

import android.content.Context
import androidx.room.Room
import com.scalo.stocklify.core.config.DatabaseConfig
import com.scalo.stocklify.core.data.database.AppDatabase
import com.scalo.stocklify.core.data.database.callback.DatabaseCallback
import com.scalo.stocklify.core.data.database.dao.CategoryDao
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.dao.StoreTypeDao
import com.scalo.stocklify.core.data.database.dao.SupplierDao
import com.scalo.stocklify.core.data.database.migration.MIGRATION_1_2
import com.scalo.stocklify.core.data.database.migration.MIGRATION_2_3
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
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .addCallback(DatabaseCallback())
            .build()
    }

    @Provides
    fun provideStoreDao(
        database: AppDatabase
    ): StoreDao {
        return database.storeDao()
    }

    @Provides
    fun provideStoreTypeDao(
        database: AppDatabase
    ): StoreTypeDao {
        return database.storeTypeDao()
    }

    @Provides
    fun provideCategoryDao(
        database: AppDatabase
    ): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideSupplierDao(
        database: AppDatabase
    ): SupplierDao {
        return database.supplierDao()
    }

    @Provides
    fun provideProductDao(
        database: AppDatabase
    ): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideStoreProductMappingDao(
        database: AppDatabase
    ): StoreProductMappingDao {
        return database.storeProductMappingDao()
    }
}