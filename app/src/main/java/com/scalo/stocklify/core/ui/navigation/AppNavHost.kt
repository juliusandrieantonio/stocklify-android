package com.scalo.stocklify.core.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.scalo.stocklify.core.util.Screen
import com.scalo.stocklify.feature.home.ui.HomeScreen
import com.scalo.stocklify.feature.salesmode.ui.SalesModeViewModel
import com.scalo.stocklify.feature.salesmode.ui.screens.CheckoutScreen
import com.scalo.stocklify.feature.salesmode.ui.screens.CurrentSalesScreen
import com.scalo.stocklify.feature.salesmode.ui.screens.SalesCompletedScreen
import com.scalo.stocklify.feature.onboarding.ui.OnboardingScreen
import com.scalo.stocklify.feature.products.ui.screens.AddEditProductScreen
import com.scalo.stocklify.feature.products.ui.screens.AddProductOptionScreen
import com.scalo.stocklify.feature.products.ui.screens.ImportSuccessfulScreen
import com.scalo.stocklify.feature.products.ui.screens.ScanProductScreen
import com.scalo.stocklify.feature.products.ui.ScanProductViewModel
import com.scalo.stocklify.feature.products.ui.screens.ReviewImportedProductScreen
import com.scalo.stocklify.feature.products.ui.screens.ScanningProductScreen
import com.scalo.stocklify.feature.products.ui.screens.StoreReadyScreen
import com.scalo.stocklify.feature.stores.ui.CreateJoinStoreScreen
import com.scalo.stocklify.feature.stores.ui.CreateStoreScreen
import com.scalo.stocklify.feature.stores.ui.JoinStoreScreen
import com.scalo.stocklify.feature.stores.ui.StoreCreatedScreen

@Composable
fun AppNavHost(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.CreateJoinStore.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        navigation(
            startDestination = Screen.Home.route,
            route = "home_flow"
        ) {
            composable(Screen.Home.route) { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry("home_flow") }
                val salesViewModel: SalesModeViewModel = hiltViewModel(parentEntry)
                HomeScreen(
                    salesViewModel = salesViewModel,
                    onAddManually = {
                        navController.navigate(Screen.AddProductManually.route())
                    },
                    onScanProduct = {
                        navController.navigate("scan_flow")
                    },
                    onCurrentSales = {
                        navController.navigate(Screen.CurrentSales.route)
                    },
                )
            }

            composable(Screen.CurrentSales.route) { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry("home_flow") }
                val salesViewModel: SalesModeViewModel = hiltViewModel(parentEntry)
                val state by salesViewModel.state.collectAsState()
                CurrentSalesScreen(
                    viewModel = salesViewModel,
                    onBack = { navController.popBackStack() },
                    onCheckout = { navController.navigate(Screen.Checkout.route(state.cartTotal)) },
                )
            }

            composable(
                route = Screen.Checkout.route,
                arguments = listOf(navArgument("total") { type = NavType.FloatType }),
            ) { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry("home_flow") }
                val salesViewModel: SalesModeViewModel = hiltViewModel(parentEntry)
                val total = entry.arguments?.getFloat("total")?.toDouble() ?: 0.0
                CheckoutScreen(
                    total = total,
                    onBack = { navController.popBackStack() },
                    onCompleteSales = { payment, change ->
                        salesViewModel.onAction(com.scalo.stocklify.feature.salesmode.ui.SalesModeAction.ClearCart)
                        navController.navigate(Screen.SalesCompleted.route(total, payment, change)) {
                            popUpTo(Screen.CurrentSales.route) { inclusive = true }
                        }
                    },
                )
            }

            composable(
                route = Screen.SalesCompleted.route,
                arguments = listOf(
                    navArgument("total") { type = NavType.FloatType },
                    navArgument("payment") { type = NavType.FloatType },
                    navArgument("change") { type = NavType.FloatType },
                ),
            ) { entry ->
                val total = entry.arguments?.getFloat("total")?.toDouble() ?: 0.0
                val payment = entry.arguments?.getFloat("payment")?.toDouble() ?: 0.0
                val change = entry.arguments?.getFloat("change")?.toDouble() ?: 0.0
                SalesCompletedScreen(
                    total = total,
                    paymentReceived = payment,
                    change = change,
                    onNewSales = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SalesCompleted.route) { inclusive = true }
                        }
                    },
                    onViewRecords = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SalesCompleted.route) { inclusive = true }
                        }
                    },
                )
            }
        }

        composable(Screen.CreateJoinStore.route) {
            CreateJoinStoreScreen(
                onCreateStore = {
                    navController.navigate(Screen.CreateStore.route)
                },
                onJoinStore = {
                    navController.navigate(Screen.JoinStore.route)
                }
            )
        }

        composable(Screen.CreateStore.route) {
            CreateStoreScreen(
                onBack = { navController.popBackStack() },
                onCreated = {
                    navController.navigate(Screen.StoreCreated.route) {
                        popUpTo(Screen.CreateStore.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.StoreCreated.route) {
            StoreCreatedScreen(
                onAddProduct = {
                    navController.navigate(Screen.AddProductOption.route)
                },
                onLater = {
                    navController.navigate("home_flow") {
                        popUpTo(Screen.StoreCreated.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AddProductOption.route) {
            AddProductOptionScreen(
                onBack = { navController.popBackStack() },
                onLater = {
                    navController.navigate("home_flow") {
                        popUpTo(Screen.AddProductOption.route) { inclusive = true }
                    }
                },
                onScanProductList = {
                    navController.navigate(Screen.ScanProduct.route)
                },
                onAddManually = {
                    navController.navigate(Screen.AddProductManually.route())
                },
            )
        }

        navigation(
            startDestination = Screen.ScanProduct.route,
            route = "scan_flow"
        ) {
            composable(Screen.ScanProduct.route) { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry("scan_flow") }
                val viewModel: ScanProductViewModel = hiltViewModel(parentEntry)
                ScanProductScreen(
                    onBack = { navController.popBackStack() },
                    onAddProduct = {
                        navController.navigate(Screen.ScanningProduct.route)
                    },
                    onLater = {
                        navController.navigate("home_flow") {
                            popUpTo("scan_flow") { inclusive = true }
                        }
                    },
                    viewModel = viewModel
                )
            }

            composable(Screen.ScanningProduct.route) { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry("scan_flow") }
                val viewModel: ScanProductViewModel = hiltViewModel(parentEntry)
                ScanningProductScreen(
                    onBack = { navController.popBackStack() },
                    onFinish = {
                        navController.navigate(Screen.ReviewImportedProduct.route)
                    },
                    viewModel = viewModel
                )
            }

            composable(Screen.ReviewImportedProduct.route) { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry("scan_flow") }
                val viewModel: ScanProductViewModel = hiltViewModel(parentEntry)
                val categories by viewModel.categories.collectAsState()

                // Receive result from AddEditProductScreen when editing a scanned product
                val editedIndex by entry.savedStateHandle
                    .getStateFlow("review_edit_index", -1)
                    .collectAsState()
                LaunchedEffect(editedIndex) {
                    if (editedIndex >= 0) {
                        val name = entry.savedStateHandle.remove<String>("review_edit_name") ?: ""
                        val price = entry.savedStateHandle.remove<String>("review_edit_price")?.toDoubleOrNull()
                        val categoryId = entry.savedStateHandle.remove<String>("review_edit_category_id")?.toLongOrNull()
                        val stock = entry.savedStateHandle.remove<String>("review_edit_stock")?.toLongOrNull()
                        val imageUri = entry.savedStateHandle.remove<String>("review_edit_image_uri")
                            ?.takeIf { it.isNotEmpty() }?.let { Uri.parse(it) }
                        entry.savedStateHandle.remove<Int>("review_edit_index")
                        viewModel.updateScannedProduct(editedIndex, name, price, categories.find { it.id == categoryId }, stock, imageUri)
                    }
                }

                ReviewImportedProductScreen(
                    onBack = { navController.popBackStack() },
                    onImport = { count ->
                        navController.navigate(Screen.ImportSuccessful.route(count)) {
                            popUpTo("scan_flow") { inclusive = true }
                        }
                    },
                    onEditManually = {
                        navController.navigate(Screen.AddProductManually.route())
                    },
                    onEditProduct = { index, product ->
                        navController.navigate(
                            Screen.AddProductManually.route(
                                name = product.productName,
                                price = product.price,
                                categoryId = product.matchedCategory?.id,
                                reviewIndex = index,
                                stock = product.stock,
                                imageUri = product.imageUri,
                            )
                        )
                    },
                    viewModel = viewModel
                )
            }

        }

        composable(
            route = Screen.ImportSuccessful.route,
            arguments = listOf(navArgument("count") { type = NavType.IntType })
        ) { entry ->
            val count = entry.arguments?.getInt("count") ?: 0
            ImportSuccessfulScreen(
                onBack = { navController.popBackStack() },
                onScanAnother = {
                    navController.navigate("scan_flow") {
                        popUpTo(Screen.ImportSuccessful.route) { inclusive = true }
                    }
                },
                onGoToInventory = {
                    navController.navigate("home_flow") {
                        popUpTo(Screen.ImportSuccessful.route) { inclusive = true }
                    }
                },
                count = count
            )
        }

        composable(
            route = Screen.AddProductManually.route,
            arguments = listOf(
                navArgument("name") { nullable = true; defaultValue = null },
                navArgument("price") { nullable = true; defaultValue = null },
                navArgument("categoryId") { nullable = true; defaultValue = null },
                navArgument("reviewIndex") { nullable = true; defaultValue = null },
                navArgument("stock") { nullable = true; defaultValue = null },
                navArgument("imageUri") { nullable = true; defaultValue = null },
            )
        ) {
            AddEditProductScreen(
                onBack = { navController.popBackStack() },
                onSave = {
                    navController.navigate(Screen.StoreReadyScreen.route) {
                        popUpTo(Screen.AddProductManually.route) { inclusive = true }
                    }
                },
                onReviewEditDone = { index, name, price, categoryId, stock, imageUri ->
                    navController.previousBackStackEntry?.savedStateHandle?.run {
                        set("review_edit_index", index)
                        set("review_edit_name", name)
                        set("review_edit_price", price?.toString())
                        set("review_edit_category_id", categoryId?.toString())
                        set("review_edit_stock", stock?.toString())
                        set("review_edit_image_uri", imageUri?.toString())
                    }
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.StoreReadyScreen.route) {
            StoreReadyScreen(
                onGoToSalesMode = {
                    navController.navigate("home_flow") {
                        popUpTo(Screen.StoreReadyScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.JoinStore.route) {
            JoinStoreScreen(
                onBack = { navController.popBackStack() },
                onJoined = {
                    navController.navigate("home_flow") {
                        popUpTo(Screen.JoinStore.route) { inclusive = true }
                    }
                }
            )
        }


    }
}