package com.scalo.stocklify.feature.stores.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.mapper.toDomain
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.model.models.Store
import com.scalo.stocklify.feature.stores.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeDao: StoreDao
) : StoreRepository {

    override suspend fun createStore(store: Store): String {
        storeDao.insert(store.toEntity())
        return store.id
    }

    override suspend fun getStoreById(id: String): Store? {
        return storeDao.getById(id)?.toDomain()
    }

    override suspend fun getStoreByCode(code: String): Store? {
        return storeDao.getByStoreCode(code)?.toDomain()
    }

    override fun getAllStores(): Flow<List<Store>> {
        return storeDao.getAll()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun getStoreCount(): Int {
        return storeDao.getStoreCount()
    }
}