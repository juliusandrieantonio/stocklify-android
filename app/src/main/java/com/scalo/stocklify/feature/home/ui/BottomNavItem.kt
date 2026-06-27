package com.scalo.stocklify.feature.home.ui

import com.scalo.stocklify.R
import androidx.annotation.DrawableRes

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val label: String
) {

    object Dashboard : BottomNavItem(
        "dashboard",
        R.drawable.outlined_dashboard,
        R.drawable.filled_dashboard,
        "Dashboard"
    )

    object Records : BottomNavItem(
        "records",
        R.drawable.outlined_transaction,
        R.drawable.filled_transaction,
        "Records"
    )

    object SalesMode : BottomNavItem(
        "salesMode",
        R.drawable.outlined_cart_2,
        R.drawable.filled_cart_2,
        "Sales Mode"
    )

    object Inventory : BottomNavItem(
        "inventory",
        R.drawable.outlined_package,
        R.drawable.filled_package,
        "Inventory"
    )

    object Profile : BottomNavItem(
        "profile",
        R.drawable.outlined_user,
        R.drawable.filled_user,
        "Profile"
    )

    companion object {
        val items = listOf(
            Dashboard,
            Records,
            SalesMode,
            Inventory,
            Profile
        )
    }
}