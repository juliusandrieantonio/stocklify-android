package com.scalo.stocklify.core.model.enums

enum class RecordStatus(val value: Int) {
    ACTIVE(1),
    INACTIVE(2),
    DELETED(3);

    companion object {
        fun from(value: Int): RecordStatus {
            return entries.first { it.value == value }
        }
    }
}