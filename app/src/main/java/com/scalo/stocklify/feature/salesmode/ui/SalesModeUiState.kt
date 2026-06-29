package com.scalo.stocklify.feature.salesmode.ui

import com.scalo.stocklify.core.model.models.Category

data class SalesModeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val categories: List<Category> = emptyList(),
    val products: List<ProductModel> = emptyList(),
    val searchQuery: String = "",
    val filter: SalesModeFilter = SalesModeFilter(),
    val cartItems: List<CartItem> = emptyList(),
) {
    val cartTotal: Double get() = cartItems.sumOf { it.subtotal }
}
