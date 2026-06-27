package com.scalo.stocklify.feature.salesmode.ui

import com.scalo.stocklify.core.model.models.Category

data class SalesModeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val categories: List<Category> = emptyList(),
    val products: List<ProductModel> = emptyList(),
    val selectedCategoryId: Long? = null,
)
