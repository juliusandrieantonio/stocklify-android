package com.scalo.stocklify.di

import android.content.Context
import androidx.room.Room
import com.scalo.stocklify.core.config.DatabaseConfig
import com.scalo.stocklify.core.data.database.AppDatabase
import com.scalo.stocklify.core.data.database.dao.StoreDao
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
            .build()
    }

    @Provides
    fun provideStoreDao(
        database: AppDatabase
    ): StoreDao {
        return database.storeDao()
    }
}