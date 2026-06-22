package com.scalo.stocklify.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.scalo.stocklify.core.data.database.util.DatabaseKeyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideDatabaseKeyManager(
        dataStore: DataStore<Preferences>
    ): DatabaseKeyManager {
        return DatabaseKeyManager(dataStore)
    }
}