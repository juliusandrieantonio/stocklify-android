package com.scalo.stocklify.core.model.enums

enum class RecordStatus(val value: String) {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    DELETED("DELETED");

    companion object {
        fun from(value: String): RecordStatus {
            return entries.first { it.value == value }
        }
    }
}