package com.scalo.stocklify.feature.products.ui

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.data.database.dao.CategoryDao
import com.scalo.stocklify.core.data.database.mapper.toDomain
import com.scalo.stocklify.core.model.models.Category
import com.scalo.stocklify.feature.products.model.ManualProductDraft
import com.scalo.stocklify.feature.products.model.ScannedProductResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

data class AddProductFormState(
    val name: String = "",
    val sellingPrice: String = "",
    val costPrice: String = "",
    val stock: String = "",
    val selectedCategory: Category? = null,
    val imageUri: Uri? = null,
    val nameError: String? = null,
    val costPriceError: String? = null,
    val sellingPriceError: String? = null,
    val stockError: String? = null,
)

sealed class SaveState {
    object Idle : SaveState()
    data class Staged(val index: Int?, val draft: ManualProductDraft) : SaveState()
    data class ReviewEditDone(
        val index: Int?,
        val name: String,
        val price: Double?,
        val categoryCode: String?,
        val stock: Int?,
        val imageUri: Uri?,
    ) : SaveState()
    data class Error(val message: String) : SaveState()
}

sealed class EditTarget {
    data class ManualDraft(val index: Int, val draft: ManualProductDraft) : EditTarget()
    data class ScanReview(val index: Int?, val result: ScannedProductResult?) : EditTarget()
}

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryDao: CategoryDao,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _formState = MutableStateFlow(
        AddProductFormState(
            name = savedStateHandle.get<String>("name")
                ?.let { Uri.decode(it) }
                ?.takeIf { it.isNotEmpty() } ?: "",
            sellingPrice = savedStateHandle.get<String>("price")
                ?.takeIf { it.isNotEmpty() } ?: "",
            costPrice = savedStateHandle.get<String>("price")
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

    private val preFilledCategoryCode: String? = savedStateHandle.get<String>("categoryCode")
        ?.takeIf { it.isNotBlank() }

    // Non-null/true when editing or appending a scanned product from ReviewImportedProductScreen
    private var reviewIndex: Int? = savedStateHandle.get<String>("reviewIndex")?.toIntOrNull()
    private var isScanReview: Boolean = reviewIndex != null

    // Holds the URI created for the pending camera capture
    private var pendingCameraUri: Uri? = null

    // Non-null when editing an already-staged manual draft from ReviewManualProductScreen
    private var manualDraftIndex: Int? = null

    init {
        loadCategories()
    }

    fun loadDraft(index: Int, draft: ManualProductDraft) {
        manualDraftIndex = index
        _formState.value = _formState.value.copy(
            name = draft.name,
            sellingPrice = draft.sellingPrice?.toString() ?: "",
            costPrice = draft.costPrice?.toString() ?: "",
            stock = draft.stock?.toString() ?: "",
            selectedCategory = draft.category,
            imageUri = draft.imageUri,
        )
    }

    fun loadScanReviewItem(index: Int?, result: ScannedProductResult?) {
        isScanReview = true
        reviewIndex = index
        if (result != null) {
            _formState.value = _formState.value.copy(
                name = result.productName ?: "",
                sellingPrice = result.price?.toString() ?: "",
                costPrice = result.price?.toString() ?: "",
                stock = result.stock?.toString() ?: "",
                selectedCategory = result.matchedCategory,
                imageUri = result.imageUri,
            )
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryDao.getAll().collect { entities ->
                val list = entities.map { it.toDomain() }
                _categories.value = list
                if (_formState.value.selectedCategory == null && preFilledCategoryCode != null) {
                    list.find { it.code == preFilledCategoryCode }?.let { cat ->
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

    fun onSellingPriceChange(value: String) {
        _formState.value = _formState.value.copy(sellingPrice = value, sellingPriceError = null)
    }

    fun onCostPriceChange(value: String) {
        _formState.value = _formState.value.copy(costPrice = value, costPriceError = null)
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
        val sellingPriceError = if (form.sellingPrice.isBlank()) "Selling price is required"
        else if (form.sellingPrice.toDoubleOrNull() == null || form.sellingPrice.toDouble() < 0) "Enter a valid price"
        else null
        val costPriceError = if (form.costPrice.isBlank()) "Cost price is required"
        else if (form.costPrice.toDoubleOrNull() == null || form.costPrice.toDouble() < 0) "Enter a valid price"
        else null
        val stockError = if (form.stock.isBlank()) "Stock quantity is required"
        else if (form.stock.toIntOrNull() == null || form.stock.toInt() < 0) "Enter a valid quantity"
        else null

        if (nameError != null || sellingPriceError != null || costPriceError != null || stockError != null) {
            _formState.value = form.copy(
                nameError = nameError,
                sellingPriceError = sellingPriceError,
                costPriceError = costPriceError,
                stockError = stockError,
            )
            return
        }

        if (isScanReview) {
            _saveState.value = SaveState.ReviewEditDone(
                index = reviewIndex,
                name = form.name.trim(),
                price = form.sellingPrice.toDoubleOrNull(),
                categoryCode = form.selectedCategory?.code,
                stock = form.stock.toIntOrNull(),
                imageUri = form.imageUri,
            )
            return
        }

        _saveState.value = SaveState.Staged(
            index = manualDraftIndex,
            draft = ManualProductDraft(
                name = form.name.trim(),
                sellingPrice = form.sellingPrice.toDoubleOrNull(),
                costPrice = form.costPrice.toDoubleOrNull(),
                stock = form.stock.toIntOrNull(),
                category = form.selectedCategory,
                imageUri = form.imageUri,
            )
        )
    }
}
