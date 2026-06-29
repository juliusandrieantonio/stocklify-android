package com.scalo.stocklify.feature.salesmode.ui

sealed interface SalesModeAction {
    data object Refresh : SalesModeAction
    data class SelectCategory(val categoryId: Long?) : SalesModeAction
    data class Search(val query: String) : SalesModeAction
    data class ApplyFilter(val filter: SalesModeFilter) : SalesModeAction
    data class AddToCart(val product: ProductModel, val quantity: Int) : SalesModeAction
    data class RemoveFromCart(val productId: Long) : SalesModeAction
    data object ClearCart : SalesModeAction
}
