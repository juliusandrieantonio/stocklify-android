package com.scalo.stocklify.feature.salesmode.ui

data class CartItem(
    val product: ProductModel,
    val quantity: Int = 1,
) {
    val subtotal: Double get() = product.sellingPrice * quantity
}
