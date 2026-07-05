package com.scalo.stocklify.feature.stores.domain.repository

import com.scalo.stocklify.core.model.models.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    suspend fun createStore(store: Store): String

    suspend fun getStoreById(id: String): Store?

    suspend fun getStoreByCode(code: String): Store?

    fun getAllStores(): Flow<List<Store>>

    suspend fun getStoreCount(): Int
}