package com.scalo.stocklify.feature.salesmode.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scalo.stocklify.core.data.database.dao.CategoryDao
import com.scalo.stocklify.core.data.database.dao.ProductDao
import com.scalo.stocklify.core.data.database.dao.StoreDao
import com.scalo.stocklify.core.data.database.dao.StoreProductMappingDao
import com.scalo.stocklify.core.data.database.mapper.toDomain
import com.scalo.stocklify.feature.products.domain.usecase.GetProductImageFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOW_STOCK_THRESHOLD = 10

@OptIn(FlowPreview::class)
@HiltViewModel
class SalesModeViewModel @Inject constructor(
    private val storeDao: StoreDao,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val storeProductMappingDao: StoreProductMappingDao,
    private val getProductImageFile: GetProductImageFileUseCase,
) : ViewModel() {

    suspend fun resolveProductImageFile(productId: String, imageUrl: String?) =
        getProductImageFile(productId, imageUrl)

    private val _state = MutableStateFlow(SalesModeUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<SalesModeEvent>()
    val event = _event.asSharedFlow()

    private var allProducts: List<ProductModel> = emptyList()
    private val _searchQuery = MutableStateFlow("")

    init {
        load()
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .collect { query ->
                    _state.update {
                        it.copy(products = filterProducts(allProducts, query, it.filter))
                    }
                }
        }
    }

    fun onAction(action: SalesModeAction) {
        when (action) {
            SalesModeAction.Refresh -> load()
            is SalesModeAction.SelectCategory -> selectCategory(action.categoryCode)
            is SalesModeAction.Search -> search(action.query)
            is SalesModeAction.ApplyFilter -> applyFilter(action.filter)
            is SalesModeAction.AddToCart -> addToCart(action.product, action.quantity)
            is SalesModeAction.RemoveFromCart -> removeFromCart(action.productId)
            SalesModeAction.ClearCart -> _state.update { it.copy(cartItems = emptyList()) }
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
                val productsById = productEntities.associate { it.id to it.toDomain() }
                val products = mappingEntities.mapNotNull { mappingEntity ->
                    val mapping = mappingEntity.toDomain()
                    val product = productsById[mapping.productId] ?: return@mapNotNull null
                    ProductModel(
                        id = product.id,
                        storeProductId = mapping.id,
                        code = mapping.productCode ?: mapping.id,
                        name = product.name,
                        sellingPrice = mapping.sellingPrice,
                        imageUrl = product.imageUrl,
                        categoryCode = mapping.categoryCode,
                        stock = mapping.quantity,
                    )
                }
                Pair(categories, products)
            }.collect { (categories, products) ->
                allProducts = products
                _state.update {
                    it.copy(
                        isLoading = false,
                        categories = categories,
                        products = filterProducts(products, it.searchQuery, it.filter),
                    )
                }
            }
        }
    }

    private fun selectCategory(categoryCode: String?) {
        _state.update {
            val updated = it.filter.copy(categoryCode = categoryCode)
            it.copy(
                filter = updated,
                products = filterProducts(allProducts, it.searchQuery, updated),
            )
        }
    }

    private fun search(query: String) {
        _state.update { it.copy(searchQuery = query) }
        _searchQuery.value = query
    }

    private fun applyFilter(filter: SalesModeFilter) {
        _state.update {
            it.copy(
                filter = filter,
                products = filterProducts(allProducts, it.searchQuery, filter),
            )
        }
    }

    private fun addToCart(product: ProductModel, quantity: Int) {
        _state.update { state ->
            val existing = state.cartItems.indexOfFirst { it.product.id == product.id }
            val updated = if (existing >= 0) {
                state.cartItems.toMutableList().also { it[existing] = it[existing].copy(quantity = quantity) }
            } else {
                state.cartItems + CartItem(product, quantity)
            }
            state.copy(cartItems = updated)
        }
    }

    private fun removeFromCart(productId: String) {
        _state.update { state ->
            state.copy(cartItems = state.cartItems.filter { it.product.id != productId })
        }
    }

    private fun filterProducts(
        products: List<ProductModel>,
        query: String,
        filter: SalesModeFilter,
    ): List<ProductModel> {
        return products
            .filter { product ->
                (filter.categoryCode == null || product.categoryCode == filter.categoryCode) &&
                (query.isBlank() || product.name.contains(query, ignoreCase = true) || product.code.contains(query, ignoreCase = true)) &&
                (filter.minPrice == null || product.sellingPrice >= filter.minPrice) &&
                (filter.maxPrice == null || product.sellingPrice <= filter.maxPrice) &&
                when (filter.stockStatus) {
                    StockStatus.All         -> true
                    StockStatus.InStock     -> product.stock > LOW_STOCK_THRESHOLD
                    StockStatus.LowStock    -> product.stock in 1..LOW_STOCK_THRESHOLD
                    StockStatus.OutOfStock  -> product.stock == 0
                }
            }
            .let { list ->
                when (filter.sortOption) {
                    SortOption.NameAsc   -> list.sortedBy { it.name }
                    SortOption.NameDesc  -> list.sortedByDescending { it.name }
                    SortOption.PriceAsc  -> list.sortedBy { it.sellingPrice }
                    SortOption.PriceDesc -> list.sortedByDescending { it.sellingPrice }
                    SortOption.StockAsc  -> list.sortedBy { it.stock }
                }
            }
    }
}
