package com.scalo.stocklify.feature.products.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.dao.StockMovementDao
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.mapper.toDto
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.data.sync.SyncOutboxWriter
import com.scalo.stocklify.core.model.enums.StockMovementReason
import com.scalo.stocklify.core.model.models.Product
import com.scalo.stocklify.core.model.models.StockMovement
import com.scalo.stocklify.core.model.models.StoreProductMapping
import com.scalo.stocklify.core.util.constants.Tables
import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import com.scalo.stocklify.feature.products.domain.usecase.UploadProductImageUseCase
import com.scalo.stocklify.feature.products.model.ManualProductDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class ManualSaveState {
    object Idle : ManualSaveState()
    object Loading : ManualSaveState()
    data class Success(val count: Int) : ManualSaveState()
    data class Error(val message: String) : ManualSaveState()
}

@HiltViewModel
class ManualAddProductViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val storeDao: StoreDao,
    private val storeProductMappingDao: StoreProductMappingDao,
    private val stockMovementDao: StockMovementDao,
    private val syncOutboxWriter: SyncOutboxWriter,
    private val authRepository: AuthRepository,
    private val uploadProductImage: UploadProductImageUseCase,
) : ViewModel() {

    private val _drafts = MutableStateFlow<List<ManualProductDraft>>(emptyList())
    val drafts: StateFlow<List<ManualProductDraft>> = _drafts.asStateFlow()

    private val _saveState = MutableStateFlow<ManualSaveState>(ManualSaveState.Idle)
    val saveState: StateFlow<ManualSaveState> = _saveState.asStateFlow()

    fun upsertDraft(index: Int?, draft: ManualProductDraft) {
        _drafts.value = if (index != null) {
            _drafts.value.toMutableList().apply { this[index] = draft }
        } else {
            _drafts.value + draft
        }
    }

    fun saveProducts() {
        val drafts = _drafts.value
        if (drafts.isEmpty()) return

        viewModelScope.launch {
            _saveState.value = ManualSaveState.Loading
            try {
                val store = storeDao.getAll().first().firstOrNull()
                    ?: throw IllegalStateException("No store found")
                val userId = authRepository.getCurrentUser()?.id

                drafts.forEach { draft ->
                    val categoryCode = draft.category?.code
                        ?: throw IllegalStateException("No category selected")
                    val now = System.currentTimeMillis()

                    val productId = UUID.randomUUID().toString()
                    val imageKey = draft.imageUri?.let { localUri ->
                        store.organizationId?.let { organizationId ->
                            uploadProductImage(localUri, organizationId, store.id, productId)
                        }
                    }

                    val product = Product(
                        id = productId,
                        organizationId = store.organizationId,
                        name = draft.name,
                        imageUrl = imageKey,
                        createdAt = now,
                        createdBy = userId,
                    )
                    productDao.insert(product.toEntity())
                    syncOutboxWriter.enqueueInsert(Tables.PRODUCTS, product.toDto())

                    val mapping = StoreProductMapping(
                        storeId = store.id,
                        productId = product.id,
                        categoryCode = categoryCode,
                        sellingPrice = draft.sellingPrice ?: 0.0,
                        costPrice = draft.costPrice ?: 0.0,
                        quantity = 0,
                        createdAt = now,
                        createdBy = userId,
                    )
                    storeProductMappingDao.insert(mapping.toEntity())
                    syncOutboxWriter.enqueueInsert(Tables.STORE_PRODUCT_MAPPING, mapping.toDto())

                    val stockMovement = StockMovement(
                        storeProductId = mapping.id,
                        quantity = draft.stock ?: 0,
                        reason = StockMovementReason.INITIAL_STOCK,
                        createdAt = now,
                        createdBy = userId,
                    )
                    stockMovementDao.insert(stockMovement.toEntity())
                    syncOutboxWriter.enqueueInsert(Tables.STOCK_MOVEMENTS, stockMovement.toDto())
                }

                _saveState.value = ManualSaveState.Success(drafts.size)
            } catch (e: Exception) {
                Log.e(TAG, "Save manual products failed", e)
                _saveState.value = ManualSaveState.Error(e.message ?: "Save failed")
            }
        }
    }

    companion object {
        private const val TAG = "ManualAddProductViewModel"
    }
}
