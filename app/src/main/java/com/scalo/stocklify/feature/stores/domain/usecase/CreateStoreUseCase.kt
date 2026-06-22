package com.scalo.stocklify.feature.stores.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.scalo.stocklify.core.model.models.Store
import com.scalo.stocklify.core.util.constants.ConstantPreferences
import com.scalo.stocklify.feature.stores.domain.repository.StoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateStoreUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val HAS_STORE_KEY = booleanPreferencesKey(ConstantPreferences.HAS_STORE_KEY)
    }

    suspend operator fun invoke(store: Store): Long {

        val id = storeRepository.createStore(store)

        dataStore.edit { prefs ->
            prefs[HAS_STORE_KEY] = true
        }

        return id
    }
}