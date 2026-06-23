package com.scalo.stocklify.feature.stores.domain.repository

import com.scalo.stocklify.core.model.models.StoreType
import kotlinx.coroutines.flow.Flow

interface StoreTypeRepository {

    fun getAllStoreTypes(): Flow<List<StoreType>>

    suspend fun getStoreTypeById(id: Long): StoreType?

    suspend fun getStoreTypeByCode(code: String): StoreType?
}
