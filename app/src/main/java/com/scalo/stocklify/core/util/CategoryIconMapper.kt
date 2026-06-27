package com.scalo.stocklify.core.util

import androidx.annotation.DrawableRes
import com.scalo.stocklify.R

@DrawableRes
fun categoryIcon(name: String): Int? = when (name) {
    "Food & Beverages"         -> R.drawable.outlined_cart_1
    "Snacks & Candy"           -> R.drawable.outlined_price_tag
    "Personal Care"            -> R.drawable.outlined_user
    "Health & Medicine"        -> R.drawable.outlined_clipboard
    "Electronics"              -> R.drawable.outlined_barcode
    "Clothing & Accessories"   -> R.drawable.outlined_crown
    "Hardware & Tools"         -> R.drawable.outlined_box
    "Household Items"          -> R.drawable.outlined_list
    "Cleaning Supplies"        -> R.drawable.outlined_package
    "School & Office Supplies" -> R.drawable.outlined_pen
    else                       -> null
}
