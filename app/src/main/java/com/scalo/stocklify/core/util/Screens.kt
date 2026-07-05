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
    object AddProductManually : Screen("addProductManually?name={name}&price={price}&categoryCode={categoryCode}&reviewIndex={reviewIndex}&stock={stock}&imageUri={imageUri}") {
        fun route(name: String? = null, price: Double? = null, categoryCode: String? = null, reviewIndex: Int? = null, stock: Int? = null, imageUri: android.net.Uri? = null): String {
            val n = name?.let { android.net.Uri.encode(it) } ?: ""
            val p = price?.toString() ?: ""
            val c = categoryCode?.let { android.net.Uri.encode(it) } ?: ""
            val r = reviewIndex?.toString() ?: ""
            val s = stock?.toString() ?: ""
            val i = imageUri?.let { android.net.Uri.encode(it.toString()) } ?: ""
            return "addProductManually?name=$n&price=$p&categoryCode=$c&reviewIndex=$r&stock=$s&imageUri=$i"
        }
    }
    object StoreReadyScreen : Screen("storeReady")
    object CurrentSales : Screen("currentSales")
    object Checkout : Screen("checkout/{total}") {
        fun route(total: Double) = "checkout/$total"
    }
    object SalesCompleted : Screen("salesCompleted/{total}/{payment}/{change}") {
        fun route(total: Double, payment: Double, change: Double) = "salesCompleted/$total/$payment/$change"
    }
}