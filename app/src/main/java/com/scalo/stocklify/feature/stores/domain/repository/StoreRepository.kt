package com.scalo.stocklify.feature.stores.domain.repository

import com.scalo.stocklify.core.data.database.entity.StoreEntity
import com.scalo.stocklify.core.model.models.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    suspend fun createStore(store: Store): Long

    suspend fun getStoreById(id: Long): Store?

    suspend fun getStoreByCode(code: String): Store?

    fun getAllStores(): Flow<List<Store>>

    suspend fun getStoreCount(): Int
}