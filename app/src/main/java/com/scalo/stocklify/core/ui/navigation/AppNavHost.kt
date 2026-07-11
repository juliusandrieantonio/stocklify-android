package com.scalo.stocklify.core.ui.navigation

import androidx.compose.runtime.Composable
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
import com.scalo.stocklify.feature.auth.ui.LoginScreen
import com.scalo.stocklify.feature.auth.ui.RegisterScreen
import com.scalo.stocklify.feature.devtools.ui.DatabaseViewerScreen
import com.scalo.stocklify.feature.onboarding.ui.WelcomeScreen
import com.scalo.stocklify.feature.preparing.ui.PreparingWorkScreen
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
import com.scalo.stocklify.feature.products.ui.screens.ReviewManualProductScreen
import com.scalo.stocklify.feature.products.ui.screens.ScanningProductScreen
import com.scalo.stocklify.feature.products.ui.screens.StoreReadyScreen
import com.scalo.stocklify.feature.products.ui.ManualAddProductViewModel
import com.scalo.stocklify.feature.premium.ui.CreateOrganizationScreen
import com.scalo.stocklify.feature.premium.ui.CreateJoinOrganizationScreen
import com.scalo.stocklify.feature.premium.ui.JoinOrganizationScreen
import com.scalo.stocklify.feature.stores.ui.CreateStoreScreen
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
                    navController.navigate(Screen.WelcomePage.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.WelcomePage.route) {
            WelcomeScreen(
                onLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.WelcomePage.route) { inclusive = true }
                    }
                },
                onContinueAsGuest = {
                    navController.navigate(Screen.CreateStore.route) {
                        popUpTo(Screen.WelcomePage.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Screen.PreparingWork.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.PreparingWork.route) {
            PreparingWorkScreen(
                onFinished = { hasOrganizationCached, hasStoreCached ->
                    val destination = when {
                        !hasOrganizationCached -> Screen.CreateJoinOrganization.route
                        !hasStoreCached -> Screen.CreateStore.route
                        else -> "home_flow"
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.PreparingWork.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegistered = {
                    navController.navigate(Screen.CreateJoinOrganization.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
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
                    onSignedOut = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onOpenDatabaseViewer = {
                        navController.navigate(Screen.DatabaseViewer.route)
                    }
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

        composable(Screen.CreateJoinOrganization.route) {
            CreateJoinOrganizationScreen(
                onCreateOrganization = {
                    navController.navigate(Screen.CreateOrganization.route)
                },
                onJoinOrganization = {
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
                        navController.navigate(Screen.ReviewImportedProduct.route) {
                            popUpTo(Screen.ScanningProduct.route) { inclusive = true }
                        }
                    },
                    viewModel = viewModel
                )
            }

            composable(Screen.ReviewImportedProduct.route) { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry("scan_flow") }
                val viewModel: ScanProductViewModel = hiltViewModel(parentEntry)

                ReviewImportedProductScreen(
                    onBack = { navController.popBackStack() },
                    onImport = { count ->
                        navController.navigate(Screen.ImportSuccessful.route(count)) {
                            popUpTo("scan_flow") { inclusive = true }
                        }
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

        navigation(
            startDestination = Screen.AddProductManually.route,
            route = "manual_add_flow"
        ) {
            composable(
                route = Screen.AddProductManually.route,
                arguments = listOf(
                    navArgument("name") { nullable = true; defaultValue = null },
                    navArgument("price") { nullable = true; defaultValue = null },
                    navArgument("categoryCode") { nullable = true; defaultValue = null },
                    navArgument("reviewIndex") { nullable = true; defaultValue = null },
                    navArgument("stock") { nullable = true; defaultValue = null },
                    navArgument("imageUri") { nullable = true; defaultValue = null },
                )
            ) { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry("manual_add_flow") }
                val manualAddViewModel: ManualAddProductViewModel = hiltViewModel(parentEntry)
                AddEditProductScreen(
                    onBack = { navController.popBackStack() },
                    onProductStaged = { index, draft ->
                        manualAddViewModel.upsertDraft(index, draft)
                        navController.navigate(Screen.ReviewManualProduct.route) {
                            popUpTo(Screen.AddProductManually.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.ReviewManualProduct.route) { entry ->
                val parentEntry = remember(entry) { navController.getBackStackEntry("manual_add_flow") }
                val manualAddViewModel: ManualAddProductViewModel = hiltViewModel(parentEntry)
                ReviewManualProductScreen(
                    viewModel = manualAddViewModel,
                    onBack = { navController.popBackStack() },
                    onSaved = {
                        navController.navigate(Screen.StoreReadyScreen.route) {
                            popUpTo("manual_add_flow") { inclusive = true }
                        }
                    }
                )
            }
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
            JoinOrganizationScreen(
                onBack = { navController.popBackStack() },
                onJoined = {
                    navController.navigate(Screen.PreparingWork.route) {
                        popUpTo(Screen.JoinStore.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.DatabaseViewer.route) {
            DatabaseViewerScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.CreateOrganization.route) {
            CreateOrganizationScreen(
                onBack = { navController.popBackStack() },
                onCreateOrganization = {
                    navController.navigate(Screen.CreateJoinOrganization.route) {
                        popUpTo(Screen.CreateStore.route) { inclusive = true }
                    }
                }
            )
        }


    }
}