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
    object ImportSuccessful : Screen("importSuccessful/{count}") {
        fun route(count: Int) = "importSuccessful/$count"
    }
    object AddProductManually : Screen("addProductManually?name={name}&price={price}&categoryId={categoryId}&reviewIndex={reviewIndex}&stock={stock}&imageUri={imageUri}") {
        fun route(name: String? = null, price: Double? = null, categoryId: Long? = null, reviewIndex: Int? = null, stock: Long? = null, imageUri: android.net.Uri? = null): String {
            val n = name?.let { android.net.Uri.encode(it) } ?: ""
            val p = price?.toString() ?: ""
            val c = categoryId?.toString() ?: ""
            val r = reviewIndex?.toString() ?: ""
            val s = stock?.toString() ?: ""
            val i = imageUri?.let { android.net.Uri.encode(it.toString()) } ?: ""
            return "addProductManually?name=$n&price=$p&categoryId=$c&reviewIndex=$r&stock=$s&imageUri=$i"
        }
    }
}