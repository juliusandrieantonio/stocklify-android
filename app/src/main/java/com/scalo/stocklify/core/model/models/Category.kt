package com.scalo.stocklify.core.model.models

import com.scalo.stocklify.core.model.enums.RecordStatus

data class Category(
    val code: String,
    val name: String,
    val description: String? = null,
    val status: RecordStatus = RecordStatus.ACTIVE,
    val version: Long = 0
)
