package com.scalo.stocklify.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.scalo.stocklify.core.util.Screen
import com.scalo.stocklify.feature.home.ui.HomeScreen
import com.scalo.stocklify.feature.onboarding.ui.OnboardingScreen
import com.scalo.stocklify.feature.products.ui.screens.AddProductOptionScreen
import com.scalo.stocklify.feature.products.ui.screens.ScanProductScreen
import com.scalo.stocklify.feature.products.ui.ScanProductViewModel
import com.scalo.stocklify.feature.products.ui.screens.ReviewImportedProductScreen
import com.scalo.stocklify.feature.products.ui.screens.ScanningProductScreen
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

        composable(Screen.Home.route) {
            HomeScreen()
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
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.StoreCreated.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AddProductOption.route) {
            AddProductOptionScreen(
                onBack = { navController.popBackStack() },
                onLater = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.AddProductOption.route) { inclusive = true }
                    }
                },
                onScanProductList = {
                    navController.navigate(Screen.ScanProduct.route)
                },
                onAddManually = {
                    // TODO: navigate to Add Product Manually screen
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
                        navController.navigate(Screen.Home.route) {
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
                ReviewImportedProductScreen(
                    onBack = { navController.popBackStack() },
                    onImport = {
                        // TODO: save selected products then navigate
                        navController.navigate(Screen.Home.route) {
                            popUpTo("scan_flow") { inclusive = true }
                        }
                    },
                    onEditManually = {
                        // TODO: navigate to manual edit screen
                    },
                    viewModel = viewModel
                )
            }
        }

        composable(Screen.JoinStore.route) {
            JoinStoreScreen(
                onBack = { navController.popBackStack() },
                onJoined = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.JoinStore.route) { inclusive = true }
                    }
                }
            )
        }


    }
}