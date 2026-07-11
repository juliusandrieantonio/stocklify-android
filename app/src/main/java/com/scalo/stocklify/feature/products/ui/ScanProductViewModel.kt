package com.scalo.stocklify.feature.products.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.data.database.dao.CategoryDao
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.dao.StockMovementDao
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.mapper.toDomain
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.model.enums.StockMovementReason
import com.scalo.stocklify.core.model.models.Category
import com.scalo.stocklify.core.model.models.Product
import com.scalo.stocklify.core.model.models.StockMovement
import com.scalo.stocklify.core.model.models.StoreProductMapping
import com.scalo.stocklify.feature.products.domain.usecase.RecognizeProductFromImageUseCase
import com.scalo.stocklify.feature.products.model.ScannedProductResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ScanningState {
    object Idle : ScanningState()
    data class Loading(val progress: Float, val message: String) : ScanningState()
    data class Success(val results: List<ScannedProductResult>) : ScanningState()
    data class Error(val message: String) : ScanningState()
}

sealed class ImportState {
    object Idle : ImportState()
    object Loading : ImportState()
    data class Success(val importedCount: Int) : ImportState()
    data class Error(val message: String) : ImportState()
}

@HiltViewModel
class ScanProductViewModel @Inject constructor(
    private val recognizeProductFromImage: RecognizeProductFromImageUseCase,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val storeDao: StoreDao,
    private val storeProductMappingDao: StoreProductMappingDao,
    private val stockMovementDao: StockMovementDao,
) : ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _scanningState = MutableStateFlow<ScanningState>(ScanningState.Idle)
    val scanningState: StateFlow<ScanningState> = _scanningState.asStateFlow()

    private val _importState = MutableStateFlow<ImportState>(ImportState.Idle)
    val importState: StateFlow<ImportState> = _importState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        viewModelScope.launch {
            categoryDao.getAll().collect { entities ->
                _categories.value = entities.map { it.toDomain() }
            }
        }
    }

    fun upsertScannedResult(index: Int?, name: String, price: Double?, category: Category?, stock: Int?, imageUri: Uri?) {
        val current = (scanningState.value as? ScanningState.Success)?.results ?: emptyList()
        val updated = if (index != null && index in current.indices) {
            current.toMutableList().apply {
                this[index] = this[index].copy(
                    productName = name.ifBlank { null },
                    price = price,
                    stock = stock,
                    imageUri = imageUri,
                    matchedCategory = category,
                )
            }
        } else {
            current + ScannedProductResult(
                rawText = "",
                productName = name.ifBlank { null },
                price = price,
                stock = stock,
                imageUri = imageUri,
                matchedCategory = category,
                suggestedProduct = null,
            )
        }
        _scanningState.value = ScanningState.Success(updated)
    }

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
        analyzeImage(uri)
    }

    private fun analyzeImage(uri: Uri) {
        viewModelScope.launch {
            try {
                _scanningState.value = ScanningState.Loading(0.1f, "Starting scanner...")

                val results = recognizeProductFromImage(
                    uri = uri,
                    onInitializing = {
                        _scanningState.value = ScanningState.Loading(0.3f, "Initializing scanner...")
                    },
                    onRunning = {
                        _scanningState.value = ScanningState.Loading(0.6f, "Analyzing image...")
                    },
                    onExtracting = {
                        _scanningState.value = ScanningState.Loading(0.85f, "Extracting products...")
                    }
                )

                _scanningState.value = ScanningState.Loading(1f, "Done!")
                Log.d(TAG, "=== OCR Scan Results (${results.size} product(s) detected) ===")
                results.forEachIndexed { index, result ->
                    Log.d(TAG, "--- Product ${index + 1} ---")
                    Log.d(TAG, "  Raw text   : ${result.rawText}")
                    Log.d(TAG, "  Name       : ${result.productName}")
                    Log.d(TAG, "  Price      : ${result.price}")
                    Log.d(TAG, "  Category   : ${result.matchedCategory?.name ?: "none"}")
                }
                _scanningState.value = ScanningState.Success(results)
            } catch (e: Exception) {
                Log.e(TAG, "OCR analysis failed", e)
                _scanningState.value = ScanningState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun importProducts(selectedIndices: Set<Int>) {
        val results = (scanningState.value as? ScanningState.Success)?.results ?: return
        viewModelScope.launch {
            _importState.value = ImportState.Loading
            try {
                val store = storeDao.getAll().first().firstOrNull()
                    ?: throw IllegalStateException("No store found")

                val valid = selectedIndices
                    .mapNotNull { results.getOrNull(it) }
                    .filter { !it.productName.isNullOrBlank() && it.price != null && it.matchedCategory != null }

                valid.forEach { result ->
                    val now = System.currentTimeMillis()

                    val product = Product(
                        organizationId = store.organizationId,
                        name = result.productName!!,
                        imageUrl = result.imageUri?.toString(),
                        createdAt = now,
                    )
                    productDao.insert(product.toEntity())

                    val mapping = StoreProductMapping(
                        storeId = store.id,
                        productId = product.id,
                        categoryCode = result.matchedCategory!!.code,
                        sellingPrice = result.price!!,
                        costPrice = result.price,
                        quantity = 0,
                        createdAt = now,
                    )
                    storeProductMappingDao.insert(mapping.toEntity())

                    stockMovementDao.insert(
                        StockMovement(
                            storeProductId = mapping.id,
                            quantity = result.stock ?: 0,
                            reason = StockMovementReason.INITIAL_STOCK,
                            createdAt = now,
                        ).toEntity()
                    )
                }

                _importState.value = ImportState.Success(valid.size)
            } catch (e: Exception) {
                Log.e(TAG, "Import failed", e)
                _importState.value = ImportState.Error(e.message ?: "Import failed")
            }
        }
    }

    companion object {
        private const val TAG = "ScanProductViewModel"
    }
}
