package com.scalo.stocklify.feature.salesmode.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.data.database.dao.CategoryDao
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesModeViewModel @Inject constructor(
    private val storeDao: StoreDao,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val storeProductMappingDao: StoreProductMappingDao,
) : ViewModel() {

    private val _state = MutableStateFlow(SalesModeUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<SalesModeEvent>()
    val event = _event.asSharedFlow()

    private var allProducts: List<ProductModel> = emptyList()

    init {
        load()
    }

    fun onAction(action: SalesModeAction) {
        when (action) {
            SalesModeAction.Refresh -> load()
            is SalesModeAction.SelectCategory -> selectCategory(action.categoryId)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val store = storeDao.getAll().first().firstOrNull()
            if (store == null) {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }

            combine(
                categoryDao.getAll(),
                productDao.getAll(),
                storeProductMappingDao.getByStoreId(store.id),
            ) { categoryEntities, productEntities, mappingEntities ->
                val categories = categoryEntities.map { it.toDomain() }
                val mappings = mappingEntities.map { it.toDomain() }
                val products = productEntities.map { entity ->
                    Log.d("SalesMode", "Categories: $categories")
                    val product = entity.toDomain()
                    val stock = mappings
                        .filter { it.productId == product.id }
                        .sumOf { it.quantity }
                    ProductModel(
                        id = product.id,
                        code = product.code,
                        name = product.name,
                        sellingPrice = product.sellingPrice,
                        imageUrl = product.imageUrl,
                        categoryId = product.categoryId,
                        stock = stock,
                    )
                }
                Pair(categories, products)
            }.collect { (categories, products) ->
                allProducts = products
                _state.update {
                    it.copy(
                        isLoading = false,
                        categories = categories,
                        products = filterProducts(products, it.selectedCategoryId),
                    )
                }
            }
        }
    }

    private fun selectCategory(categoryId: Long?) {
        _state.update {
            it.copy(
                selectedCategoryId = categoryId,
                products = filterProducts(allProducts, categoryId),
            )
        }
    }

    private fun filterProducts(products: List<ProductModel>, categoryId: Long?): List<ProductModel> {
        return if (categoryId == null) products
        else products.filter { it.categoryId == categoryId }
    }
}
