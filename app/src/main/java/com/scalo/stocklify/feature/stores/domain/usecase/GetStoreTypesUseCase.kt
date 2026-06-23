package com.scalo.stocklify.feature.stores.domain.usecase

import com.scalo.stocklify.core.model.models.StoreType
import com.scalo.stocklify.feature.stores.domain.repository.StoreTypeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoreTypesUseCase @Inject constructor(
    private val storeTypeRepository: StoreTypeRepository
) {

    operator fun invoke(): Flow<List<StoreType>> {
        return storeTypeRepository.getAllStoreTypes()
    }
}
