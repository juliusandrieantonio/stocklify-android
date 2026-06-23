package com.scalo.stocklify.feature.stores.data.repository

import com.scalo.stocklify.core.data.database.dao.StoreTypeDao
import com.scalo.stocklify.core.data.database.mapper.toDomain
import com.scalo.stocklify.core.model.models.StoreType
import com.scalo.stocklify.feature.stores.domain.repository.StoreTypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreTypeRepositoryImpl @Inject constructor(
    private val storeTypeDao: StoreTypeDao
) : StoreTypeRepository {

    override fun getAllStoreTypes(): Flow<List<StoreType>> {
        return storeTypeDao.getAll()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getStoreTypeById(id: Long): StoreType? {
        return storeTypeDao.getById(id)?.toDomain()
    }

    override suspend fun getStoreTypeByCode(code: String): StoreType? {
        return storeTypeDao.getByCode(code)?.toDomain()
    }
}
