package com.scalo.stocklify.core.util

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object CreateStore : Screen("store")
    object StoreCreated : Screen("storeCreated")
    object JoinStore : Screen("join")
    object CreateJoinStore : Screen("createJoin")
    object AddProductOption : Screen("addProductOption")
    object ScanProduct : Screen("scanProduct")
    object ScanningProduct : Screen("scanningProduct")
    object ReviewImportedProduct : Screen("reviewImportedProduct")
    object AddProductManually : Screen("addProductManually")
}