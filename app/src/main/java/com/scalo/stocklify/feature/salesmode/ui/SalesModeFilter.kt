package com.scalo.stocklify.feature.salesmode.ui

enum class SortOption(val label: String) {
    NameAsc("Name A to Z"),
    NameDesc("Name Z to A"),
    PriceAsc("Price Low to High"),
    PriceDesc("Price High to Low"),
    StockAsc("Stock Low to High"),
}

enum class StockStatus(val label: String) {
    All("All"),
    InStock("In Stock"),
    LowStock("Low Stock"),
    OutOfStock("Out of Stock"),
}

data class SalesModeFilter(
    val sortOption: SortOption = SortOption.NameAsc,
    val stockStatus: StockStatus = StockStatus.All,
    val categoryId: Long? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
) {
    val activeCount: Int get() = listOf(
        sortOption != SortOption.NameAsc,
        stockStatus != StockStatus.All,
        categoryId != null,
        minPrice != null,
        maxPrice != null,
    ).count { it }

    val isActive: Boolean get() = activeCount > 0
}
