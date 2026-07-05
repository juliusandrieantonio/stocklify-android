package com.scalo.stocklify.core.model.enums

enum class SyncStatus(val value: String) {
    SYNCED("SYNCED"),
    PENDING_PUSH("PENDING_PUSH"),
    PUSH_FAILED("PUSH_FAILED");

    companion object {
        fun from(value: String): SyncStatus {
            return entries.first { it.value == value }
        }
    }
}
