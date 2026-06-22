package com.scalo.stocklify.core.data.database.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.scalo.stocklify.core.util.constants.ConstantPreferences
import kotlinx.coroutines.flow.first
import java.util.UUID

class DatabaseKeyManager(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val DB_KEY = stringPreferencesKey(ConstantPreferences.DB_KEY)
    }

    suspend fun getOrCreateKey(): String {
        val stored = dataStore.data.first()[Keys.DB_KEY]

        if (stored != null) return stored

        val newKey = UUID.randomUUID().toString()

        dataStore.edit { prefs ->
            prefs[Keys.DB_KEY] = newKey
        }

        return newKey
    }
}