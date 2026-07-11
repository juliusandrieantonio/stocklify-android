package com.scalo.stocklify.feature.products.model

import android.net.Uri
import com.scalo.stocklify.core.model.models.Category

data class ManualProductDraft(
    val name: String,
    val sellingPrice: Double?,
    val costPrice: Double?,
    val stock: Int?,
    val category: Category?,
    val imageUri: Uri?,
)
