package com.scalo.stocklify.feature.products.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.data.database.dao.CategoryDao
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.entity.ProductEntity
import com.scalo.stocklify.core.data.database.entity.StoreProductMappingEntity
import com.scalo.stocklify.core.data.database.mapper.toDomain
import com.scalo.stocklify.core.model.enums.RecordStatus
import com.scalo.stocklify.core.model.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

data class AddProductFormState(
    val name: String = "",
    val price: String = "",
    val stock: String = "",
    val selectedCategory: Category? = null,
    val imageUri: Uri? = null,
    val nameError: String? = null,
    val priceError: String? = null,
    val stockError: String? = null,
)

sealed class SaveState {
    object Idle : SaveState()
    object Loading : SaveState()
    object Success : SaveState()
    data class ReviewEditDone(
        val index: Int,
        val name: String,
        val price: Double?,
        val categoryId: Long?,
        val stock: Long?,
        val imageUri: Uri?,
    ) : SaveState()
    data class Error(val message: String) : SaveState()
}

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val storeDao: StoreDao,
    private val storeProductMappingDao: StoreProductMappingDao,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _formState = MutableStateFlow(
        AddProductFormState(
            name = savedStateHandle.get<String>("name")
                ?.let { Uri.decode(it) }
                ?.takeIf { it.isNotEmpty() } ?: "",
            price = savedStateHandle.get<String>("price")
                ?.takeIf { it.isNotEmpty() } ?: "",
            stock = savedStateHandle.get<String>("stock")
                ?.takeIf { it.isNotEmpty() } ?: "",
            imageUri = savedStateHandle.get<String>("imageUri")
                ?.takeIf { it.isNotEmpty() }
                ?.let { Uri.parse(Uri.decode(it)) },
        )
    )
    val formState: StateFlow<AddProductFormState> = _formState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    private val preFilledCategoryId: Long? = savedStateHandle.get<String>("categoryId")
        ?.toLongOrNull()
        ?.takeIf { it > 0 }

    // Non-null when launched from ReviewImportedProductScreen to edit a scanned product
    val reviewIndex: Int? = savedStateHandle.get<String>("reviewIndex")?.toIntOrNull()

    // Holds the URI created for the pending camera capture
    private var pendingCameraUri: Uri? = null

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryDao.getAll().collect { entities ->
                val list = entities.map { it.toDomain() }
                _categories.value = list
                if (_formState.value.selectedCategory == null && preFilledCategoryId != null) {
                    list.find { it.id == preFilledCategoryId }?.let { cat ->
                        _formState.value = _formState.value.copy(selectedCategory = cat)
                    }
                }
            }
        }
    }

    fun createCameraUri(): Uri {
        val file = File(context.cacheDir, "product_${UUID.randomUUID()}.jpg")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        pendingCameraUri = uri
        return uri
    }

    fun onCameraImageCaptured() {
        val uri = pendingCameraUri ?: return
        viewModelScope.launch {
            val savedUri = withContext(Dispatchers.IO) { copyToInternalStorage(uri) }
            _formState.value = _formState.value.copy(imageUri = savedUri)
        }
    }

    fun onImageSelected(uri: Uri) {
        viewModelScope.launch {
            val savedUri = withContext(Dispatchers.IO) { copyToInternalStorage(uri) }
            _formState.value = _formState.value.copy(imageUri = savedUri)
        }
    }

    private fun copyToInternalStorage(sourceUri: Uri): Uri {
        val dir = File(context.filesDir, "product_images").also { it.mkdirs() }
        val file = File(dir, "${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        return Uri.fromFile(file)
    }

    fun onNameChange(value: String) {
        _formState.value = _formState.value.copy(name = value, nameError = null)
    }

    fun onPriceChange(value: String) {
        _formState.value = _formState.value.copy(price = value, priceError = null)
    }

    fun onStockChange(value: String) {
        _formState.value = _formState.value.copy(stock = value, stockError = null)
    }

    fun onCategorySelected(category: Category) {
        _formState.value = _formState.value.copy(selectedCategory = category)
    }

    fun save() {
        val form = _formState.value
        val nameError = if (form.name.isBlank()) "Product name is required" else null
        val priceError = if (form.price.isNotBlank() &&
            (form.price.toDoubleOrNull() == null || form.price.toDouble() < 0)) "Enter a valid price"
        else null
        val stockError = if (form.stock.isBlank()) "Stock quantity is required"
        else if (form.stock.toLongOrNull() == null || form.stock.toLong() < 0) "Enter a valid quantity"
        else null

        if (nameError != null || priceError != null || stockError != null) {
            _formState.value = form.copy(
                nameError = nameError,
                priceError = priceError,
                stockError = stockError,
            )
            return
        }

        if (reviewIndex != null) {
            _saveState.value = SaveState.ReviewEditDone(
                index = reviewIndex,
                name = form.name.trim(),
                price = form.price.toDoubleOrNull(),
                categoryId = form.selectedCategory?.id,
                stock = form.stock.toLongOrNull(),
                imageUri = form.imageUri,
            )
            return
        }

        viewModelScope.launch {
            _saveState.value = SaveState.Loading
            try {
                val store = storeDao.getAll().first().firstOrNull()
                    ?: throw IllegalStateException("No store found")

                val categoryId = form.selectedCategory?.id
                    ?: throw IllegalStateException("No category selected")

                val productId = productDao.insert(
                    ProductEntity(
                        code = UUID.randomUUID().toString(),
                        name = form.name.trim(),
                        sellingPrice = form.price.toDouble(),
                        costPrice = form.price.toDouble(),
                        imageUrl = form.imageUri?.toString(),
                        categoryId = categoryId,
                        createdAt = System.currentTimeMillis(),
                        createdBy = "",
                        updatedAt = null,
                        updatedBy = null,
                        status = RecordStatus.ACTIVE,
                        version = 1,
                    )
                )

                storeProductMappingDao.insert(
                    StoreProductMappingEntity(
                        storeId = store.id,
                        productId = productId,
                        quantity = form.stock.toLong(),
                        supplierId = null,
                        createdAt = System.currentTimeMillis(),
                        createdBy = "",
                        updatedAt = null,
                        updatedBy = null,
                        status = RecordStatus.ACTIVE,
                        version = 1,
                    )
                )

                _saveState.value = SaveState.Success
            } catch (e: Exception) {
                Log.e(TAG, "Save product failed", e)
                _saveState.value = SaveState.Error(e.message ?: "Save failed")
            }
        }
    }

    companion object {
        private const val TAG = "AddEditProductViewModel"
    }
}
