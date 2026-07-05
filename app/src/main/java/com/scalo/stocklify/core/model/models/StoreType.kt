package com.scalo.stocklify.core.model.models

import com.scalo.stocklify.core.model.enums.RecordStatus

data class StoreType(
    val code: String,
    val name: String,
    val status: RecordStatus = RecordStatus.ACTIVE,
    val version: Long = 0
)
