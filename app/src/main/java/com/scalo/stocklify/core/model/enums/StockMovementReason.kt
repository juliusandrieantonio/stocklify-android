package com.scalo.stocklify.core.model.enums

enum class StockMovementReason(val value: String) {
    INITIAL_STOCK("INITIAL_STOCK"),
    SALE("SALE"),
    RESTOCK("RESTOCK"),
    ADJUSTMENT("ADJUSTMENT"),
    RETURN("RETURN");

    companion object {
        fun from(value: String): StockMovementReason {
            return entries.first { it.value == value }
        }
    }
}
